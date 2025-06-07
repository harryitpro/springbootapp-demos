package com.myapp.poc.jms.client;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final JmsTemplate jmsQueueTemplate; // Default for queues
    private final JmsTemplate jmsTopicTemplate; // For topics

    public void sendToQueue(String message) {
        jmsQueueTemplate.convertAndSend("demo-queue", message);
        System.out.println("Sent to queue: " + message);
    }

    public void sendToTopic(String message) {
        jmsTopicTemplate.convertAndSend("demo-topic", message);
        System.out.println("Sent to topic: " + message);
    }
}
