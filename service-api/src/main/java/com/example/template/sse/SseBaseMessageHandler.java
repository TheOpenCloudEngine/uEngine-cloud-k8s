package com.example.template.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("messageHandler")
public class SseBaseMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SseBaseMessageHandler.class);

    private ApplicationEventPublisher eventPublisher;
    public SseBaseMessageHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Async
    public void publish(String instanceType, String jsonData) {
        try {

            SseBaseMessage message = new SseBaseMessage();
            message.setInstanceType(instanceType);
            message.setMessage(jsonData);
            this.eventPublisher.publishEvent(message);
        } catch (Exception ex) {
            LOGGER.error("Publish Error");
        }
    }
}
