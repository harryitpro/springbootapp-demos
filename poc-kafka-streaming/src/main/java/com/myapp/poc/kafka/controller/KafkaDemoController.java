package com.myapp.poc.kafka.controller;

import com.myapp.poc.kafka.client.KafkaEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kafka/messages")
public class KafkaDemoController {

    private final KafkaEventProducer producerService;

    @PostMapping(
            consumes = "application/json"
    )
    public String sendMessage(@RequestBody String message) {
        producerService.sendMessage(message);
        return "Message sent: " + message;
    }
}
