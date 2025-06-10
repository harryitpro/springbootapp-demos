package com.myapp.jms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.jms.model.MessagePayload;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

@Component
public class JmsConsumer {

    private final ObjectMapper objectMapper;

    public JmsConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "demoQueue")
    public void receiveMessage(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String json = textMessage.getText();
                MessagePayload payload = objectMapper.readValue(json, MessagePayload.class);
                System.out.println("Received message: " + payload);

                // Manually acknowledge the message
                message.acknowledge();
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
                // Optionally, handle the error without acknowledging
            }
        }
    }
}