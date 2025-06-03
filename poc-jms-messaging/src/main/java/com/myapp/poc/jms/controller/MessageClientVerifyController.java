package com.myapp.poc.jms.controller;

import com.myapp.poc.jms.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jms")
@RequiredArgsConstructor
public class MessageClientVerifyController {

    private final MessageProducer messageProducer;

    @PostMapping(
            value = "/queue",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public String sendToQueue(@RequestBody String message) {
        messageProducer.sendToQueue(message);
        return "Message sent to queue: " + message;
    }

    @PostMapping(
            value ="/topic",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public String sendToTopic(@RequestBody String message) {
        messageProducer.sendToTopic(message);
        return "Message sent to topic: " + message;
    }
}
