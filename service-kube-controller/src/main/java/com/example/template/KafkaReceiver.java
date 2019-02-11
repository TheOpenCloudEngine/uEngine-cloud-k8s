package com.example.template;

import com.squareup.okhttp.MediaType;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.util.Config;
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
import org.yaml.snakeyaml.Yaml;


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

//        System.out.println(message);
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse( message );
            JSONObject jsonObj = (JSONObject) obj;
//            String host = (String) jsonObj.get("host");
//            String token = (String) jsonObj.get("token");
            String type = (String) jsonObj.get("type");
            String command = (String) jsonObj.get("command");
//            Object body = (Object) jsonObj.get("body");
//
//            LOG.info("message host='{}'" , host);
//            LOG.info("message token='{}'" , token);
//            LOG.info("message type='{}'" , type);
//            LOG.info("message command='{}'" , command);
//            LOG.info("message body='{}'" , body);

            this.sendMessage(type, "처리중");

            if( type.equals("DEPLOY") && command.equals("CREATE")){
                this.createDeploy(jsonObj);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String type, String command){
        String msg = type + " : " + command;
        kafkaTemplate.send(stateMsgTopic , msg);
    }

    public void createDeploy(JSONObject jsonObj){
        String host = (String) jsonObj.get("host");
        String token = (String) jsonObj.get("token");
        String type = (String) jsonObj.get("type");
        String namespace = (String) jsonObj.get("namespace");
        if( namespace == null ){
            namespace = "default";
        }
        Object body = (Object) jsonObj.get("body");
        String bodyTmp = body.toString();
        bodyTmp = bodyTmp.replaceAll("\\\\", "");
        LOG.info("message bodyTmp='{}'" , bodyTmp);
        try {
            Yaml yaml = new Yaml();
            V1Deployment deploy = yaml.loadAs(bodyTmp, V1Deployment.class);
//        LOG.info("message getMetadata='{}'" , deploy.toString());
//        LOG.info("message getSpec='{}'" , deploy.getSpec().toString());

            ApiClient client = Config.fromToken(host, token, false);
//            client.setDebugging(true);
            AppsV1Api apiInstance = new AppsV1Api(client);

            V1Deployment result = apiInstance.createNamespacedDeployment(namespace, deploy, null, null, null);
            System.out.println(result);

        }catch (Exception e){
            e.printStackTrace();
            this.sendMessage(type , "처리실패");
        }
    }

}
