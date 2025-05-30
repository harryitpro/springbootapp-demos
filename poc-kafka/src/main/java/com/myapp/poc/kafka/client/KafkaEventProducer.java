package com.myapp.poc.kafka.client;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventProducer {
    private static final String TOPIC = "demo-topic-partition";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        String key = "key-" + System.currentTimeMillis(); // Unique key for each message
        kafkaTemplate.send(TOPIC, key, message);
        System.out.println("Message sent to topic " + TOPIC + " with key " + key + ": " + message);
    }
}
