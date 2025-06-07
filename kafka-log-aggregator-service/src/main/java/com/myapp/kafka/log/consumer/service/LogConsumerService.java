package com.myapp.kafka.log.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LogConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(LogConsumerService.class);

    @KafkaListener(topics = "${app.kafka.topic.logs}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        // Here you would process the log message
        // For this demo, we'll just print it to the console.
        // In a real application, you might:
        // - Parse the log format (e.g., JSON, Grok)
        // - Store it in Elasticsearch, Splunk, or a database
        // - Send it to an alerting system
        // - Perform real-time analytics

        logger.info("Received log from Kafka [Topic: {}, Partition: {}, Offset: {}, Key: {}]: {}",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());

        // Example of simple processing: check for ERROR level
        if (record.value().contains("[ERROR]")) {
            logger.error("!!! DETECTED ERROR LOG: {}", record.value());
            // Trigger an alert or further action
        }
    }
}
