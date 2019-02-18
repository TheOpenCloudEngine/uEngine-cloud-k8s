package com.example.template.k8s.pod;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.template.k8s.deployment.Deployment;
import com.example.template.k8s.deployment.DeploymentService;
import com.example.template.k8s.pod.PodService;
import com.example.template.sse.SseBaseMessageHandler;
import com.google.gson.Gson;



@Service
public class PodKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(PodKafkaService.class);

    @Autowired
    PodService podService;
    

    @Autowired
    private SseBaseMessageHandler messageHandler;



    /**
     * 특정 클래스로 받으려면 아래와 같이 객체로 변환을 하면 된다.
     * 주의점은 프로듀서쪽의 객체를 잘 마추어야 한다. - 별로 추천하지 않음
     * @param message
     * @param consumerRecord
     */

    @KafkaListener(topics = "${topic.podeMsgTopic}")
    public void listenByObject(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

        System.out.println(message);
        Gson gson = new Gson();
        Pod pod = gson.fromJson(message, Pod.class);
        
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss.SSS");
    	Calendar cal = Calendar.getInstance();
    	String today = null;
    	today = formatter.format(cal.getTime());
    	Timestamp ts = Timestamp.valueOf(today);
    	pod.setCreateTime(ts);
        
        podService.update(pod);
        
        messageHandler.publish("pod", message);
        
    }

}
