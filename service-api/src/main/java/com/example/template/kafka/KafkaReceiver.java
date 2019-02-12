package com.example.template.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.template.deployment.Deployment;
import com.example.template.deployment.DeploymentService;
import com.example.template.pod.PodService;
import com.example.template.sse.SseBaseMessageHandler;
import com.google.gson.Gson;



@Service
public class KafkaReceiver {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaReceiver.class);

    @Autowired
    PodService instanceService;
    
    @Autowired
    private DeploymentService deploymentService;
    

    @Autowired
    private SseBaseMessageHandler messageHandler;



    /**
     * 특정 클래스로 받으려면 아래와 같이 객체로 변환을 하면 된다.
     * 주의점은 프로듀서쪽의 객체를 잘 마추어야 한다. - 별로 추천하지 않음
     * @param message
     * @param consumerRecord
     */

    @KafkaListener(topics = "${topic.stateMsgTopic}")
    public void listenByObject(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

        System.out.println(message);
//        Gson gson = new Gson();
//        InstanceModel im = gson.fromJson(message, InstanceModel.class);
//        instanceService.update(im);
//        instanceService.deleteCacheList(im);
//        messageHandler.publish(im.getName(), im.getProvider(), message);
    }
    
    @KafkaListener(topics = "${topic.delpoyMsgTopic}")
    public void listenByDeployment(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

    	Gson gson = new Gson();
    	Deployment dpl = gson.fromJson(message, Deployment.class);
    	
    	deploymentService.update(dpl);
    	
    	messageHandler.publish(dpl.getName(), dpl.getProvider(), message);
    	
        System.out.println(message);
    }

}
