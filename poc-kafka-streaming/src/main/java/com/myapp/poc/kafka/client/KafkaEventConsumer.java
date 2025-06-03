package com.myapp.poc.kafka.client;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventConsumer {
    @KafkaListener(topics = "demo-topic-partition", groupId = "demo-group", concurrency = "3")
    public void consume(ConsumerRecord<String, String> record) {
        String threadId = Thread.currentThread().getName();
        String message = record.value();
        int partition = record.partition();
        System.out.println("Consumer [Thread-" + threadId + "] received message from partition [" + partition + "]: " + message);
    }
}
