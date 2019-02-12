package com.example.template.k8s.deployment;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.template.sse.SseBaseMessageHandler;
import com.google.gson.Gson;



@Service
public class DeploymentKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(DeploymentKafkaService.class);
    
    @Autowired
    private DeploymentService deploymentService;
    
    @Autowired
    private SseBaseMessageHandler messageHandler;


    @KafkaListener(topics = "${topic.delpoyMsgTopic}")
    public void listenByDeployment(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

    	Gson gson = new Gson();
    	Deployment dpl = gson.fromJson(message, Deployment.class);
    	
    	deploymentService.update(dpl);
    	
//    	messageHandler.publish(dpl.getName(), dpl.getProvider(), message);
    	
        System.out.println(message);
    }

}
