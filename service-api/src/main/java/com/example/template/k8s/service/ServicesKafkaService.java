package com.example.template.k8s.service;

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
public class ServicesKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicesKafkaService.class);
    
    @Autowired
    private ServicesService deploymentService;
    
    @Autowired
    private SseBaseMessageHandler messageHandler;


    @KafkaListener(topics = "${topic.delpoyMsgTopic}")
    public void listenByDeployment(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

    	Gson gson = new Gson();
    	Services dpl = gson.fromJson(message, Services.class);
    	
    	deploymentService.update(dpl);
    	
//    	messageHandler.publish(dpl.getName(), dpl.getProvider(), message);
    	
        System.out.println(message);
    }

}
