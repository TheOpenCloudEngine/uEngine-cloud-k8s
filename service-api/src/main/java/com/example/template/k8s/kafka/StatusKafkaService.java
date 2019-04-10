package com.example.template.k8s.kafka;

import com.example.template.k8s.deployment.DeploymentService;
import com.example.template.k8s.pod.PodService;
import com.example.template.k8s.service.ServicesService;
import com.example.template.sse.SseBaseMessageHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class StatusKafkaService {

    private static final Logger LOG = LoggerFactory.getLogger(StatusKafkaService.class);

    @Autowired
    private PodService podService;
    @Autowired
    private ServicesService servicesService;
    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private SseBaseMessageHandler messageHandler;

    @KafkaListener(topics = "${topic.statusTopic}")
    public void listenByServices(@Payload String message, ConsumerRecord<?, ?> consumerRecord) throws ParseException {
        String host = (String)consumerRecord.key();

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(message);

        LOG.info(json.toString());

        if( "DELETE_ALL".equals(json.get("code"))){
            podService.deleteByHost(host);
            deploymentService.deleteByHost(host);
            servicesService.deleteByHost(host);
            return;
        }

//        messageHandler.publish("service", json, svs.getNamespace(), svs.getHost());

    }
}
