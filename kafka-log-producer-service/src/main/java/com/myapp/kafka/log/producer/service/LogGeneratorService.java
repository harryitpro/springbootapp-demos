package com.myapp.kafka.log.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LogGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(LogGeneratorService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic.logs}")
    private String logsTopic;

    private final Random random = new Random();

    public LogGeneratorService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 2000) // Generate a log every 2 seconds
    public void generateAndSendLog() {
        String logMessage = createRandomLogMessage();
        logger.info("Generated log: {}", logMessage); // Also log locally

        // Send the log message to Kafka
        kafkaTemplate.send(logsTopic, "producer-key", logMessage);
    }

    private String createRandomLogMessage() {
        String[] logLevels = {"INFO", "WARN", "ERROR", "DEBUG"};
        String[] services = {"UserService", "ProductService", "OrderService", "PaymentService"};
        String[] messages = {
                "Request processed successfully.",
                "Database connection failed.",
                "Invalid input detected.",
                "User login attempt.",
                "Resource not found.",
                "Transaction committed.",
                "Unexpected error occurred.",
                "Cache refreshed."
        };

        String level = logLevels[random.nextInt(logLevels.length)];
        String service = services[random.nextInt(services.length)];
        String message = messages[random.nextInt(messages.length)];
        long timestamp = System.currentTimeMillis();

        return String.format("[%s] - %d - %s - %s: %s", level, timestamp, service, Thread.currentThread().getName(), message);
    }
}
