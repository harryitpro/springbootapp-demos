package com.myapp.jms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.jms.model.MessagePayload;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsProducer {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public JmsProducer(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String destination, MessagePayload payload) throws Exception {
        String jsonMessage = objectMapper.writeValueAsString(payload);
        jmsTemplate.convertAndSend(destination, jsonMessage);
    }
}