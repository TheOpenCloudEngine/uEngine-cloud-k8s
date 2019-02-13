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
    private ServicesService servicesService;
    
    @Autowired
    private SseBaseMessageHandler messageHandler;


    @KafkaListener(topics = "${topic.serviceMsgTopic}")
    public void listenByServices(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {

    	Gson gson = new Gson();
    	Services svs = gson.fromJson(message, Services.class);
    	servicesService.update(svs);
    	
//    	messageHandler.publish(dpl.getName(), dpl.getProvider(), message);
    	
        System.out.println(message);
    }

}
