package com.myapp.jms.controller;

import com.myapp.jms.model.MessagePayload;
import com.myapp.jms.service.JmsProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jms")
public class JmsController {

    private final JmsProducer jmsProducer;

    public JmsController(JmsProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody MessagePayload payload) {
        try {
            jmsProducer.sendMessage("demoQueue", payload);
            return "Message sent successfully";
        } catch (Exception e) {
            return "Error sending message: " + e.getMessage();
        }
    }
}