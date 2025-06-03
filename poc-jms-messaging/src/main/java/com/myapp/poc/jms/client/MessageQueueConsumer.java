package com.myapp.poc.jms.client;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageQueueConsumer {

    @JmsListener(destination = "demo-queue", containerFactory = "queueListenerFactory")
    public void receiveQueueMessage(String message) {
        System.out.println("Queue Consumer received: " + message);
    }
}
