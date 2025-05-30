package com.myapp.poc.jms.client;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageTopicConsumer {

    @JmsListener(destination = "demo-topic", containerFactory = "topicListenerFactory")
    public void receiveTopicMessage(String message) {
        System.out.println("Topic Consumer 1 received: " + message);
    }
}
