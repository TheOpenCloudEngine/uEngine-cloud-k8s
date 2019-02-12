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

    @Autowired
    private SseRestController sseController;
    private ApplicationEventPublisher eventPublisher;
    public SseBaseMessageHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Async
    public void publish(String name, String provider, String jsonData) {
        try {

            SseBaseMessage message = new SseBaseMessage();
            message.setName(provider);
            message.setName(name);
            message.setMessage(jsonData);
            LOGGER.error("Published");
            this.eventPublisher.publishEvent(message);
        } catch (Exception ex) {
            LOGGER.error("Publish Error");
        }
    }
}
