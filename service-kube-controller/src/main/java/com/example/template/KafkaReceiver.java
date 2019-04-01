package com.example.template;

import com.example.template.manager.DeployManager;
import com.example.template.manager.KubeManager;
import com.example.template.manager.PodManager;
import com.example.template.manager.ServiceManager;
import com.squareup.okhttp.MediaType;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaReceiver.class);

    @Value("${topic.orderTopic}")
    private String orderTopic;

    @Value("${topic.stateMsgTopic}")
    private String stateMsgTopic;

    @Autowired
    KafkaTemplate kafkaTemplate;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * 특정 클래스로 받으려면 아래와 같이 객체로 변환을 하면 된다.
     * 주의점은 프로듀서쪽의 객체를 잘 마추어야 한다. - 별로 추천하지 않음
     * @param message
     * @param consumerRecord
     */

    @KafkaListener(topics = "${topic.orderTopic}")
    public void listenByObject(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

        System.out.println(message);
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse( message );
            JSONObject jsonObj = (JSONObject) obj;
            String host = (String) jsonObj.get("host");
            String token = (String) jsonObj.get("token");
            String type = (String) jsonObj.get("type");
            String command = (String) jsonObj.get("command");

            this.sendMessage(type, "처리중");

            KubeManager manager = null;
            if( type.equals("SERVICE")){
                manager = new ServiceManager(host, token);
            }else if( type.equals("DEPLOY")){
                manager = new DeployManager(host, token);
            }else if( type.equals("POD")){
                manager = new PodManager(host, token);
            }


            if ( command.equals("CREATE")){
                manager.create(jsonObj);
            }else if( command.equals("DELETE")){
                manager.delete(jsonObj);
            }else if( command.equals("UPDATE")){
                manager.update(jsonObj);
            }

        } catch (ParseException e) {
            this.sendMessage(e.toString(), "처리실패");
            e.printStackTrace();
        }
    }

    public void sendMessage(String type, String command){
        // TODO 추후 적용
//        String msg = command + " : " + type;
//        kafkaTemplate.send(stateMsgTopic , msg);
    }

}
