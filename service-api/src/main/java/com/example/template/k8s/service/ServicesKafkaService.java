package com.example.template.k8s.service;

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
    	
    	SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    	Calendar cal = Calendar.getInstance();
    	String today = null;
    	today = formatter.format(cal.getTime());
    	Timestamp ts = Timestamp.valueOf(today);
    	svs.setCreateTime(ts);
    	
    	servicesService.update(svs);
    	messageHandler.publish("service", message);
        System.out.println(message);
    }
    

}
