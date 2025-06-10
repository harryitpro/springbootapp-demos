// JmsConsumer.java
// This component contains various JmsListener methods demonstrating
// different acknowledgment modes and message types.

package com.myapp.jms.service;

import com.myapp.jms.model.Order;
import jakarta.jms.JMSException; // Correct Jakarta EE JMS API import
import jakarta.jms.Message; // Correct Jakarta EE JMS API import
import jakarta.jms.TextMessage; // Correct Jakarta EE JMS API import
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {

    // Listener for 'myQueue' (default: SESSION_TRANSACTED, auto-acknowledged on success)
    // If an unchecked exception is thrown, the message will be redelivered.
    @JmsListener(destination = "myQueue")
    public void receivePlainText(String message) {
        System.out.println("Consumer (myQueue - Default Transactional): Received plain text: " + message);
    }

    // Listener for 'orderQueue' (default: SESSION_TRANSACTED, auto-acknowledged on success)
    // Uses the MappingJackson2MessageConverter to automatically deserialize JSON to Order POJO.
    @JmsListener(destination = "orderQueue")
    public void receiveOrderObject(Order order) {
        System.out.println("Consumer (orderQueue - Default Transactional): Received Order object: " + order);
        System.out.println("  Order ID: " + order.getOrderId());
        System.out.println("  Item: " + order.getItem());
        // If an unchecked exception is thrown here, the message will be redelivered.
    }

    // Listener for 'clientAckQueue' (configured with myClientAckFactory for CLIENT_ACKNOWLEDGE)
    // Requires 'jakarta.jms.Message' parameter to manually acknowledge the message.
    @JmsListener(destination = "clientAckQueue", containerFactory = "myClientAckFactory")
    public void receiveClientAckMessage(Message message) {
        try {
            String textContent = null;
            if (message instanceof TextMessage) {
                textContent = ((TextMessage) message).getText();
            } else {
                textContent = "Non-TextMessage received: " + message.getClass().getName();
            }

            System.out.println("Consumer (clientAckQueue - CLIENT_ACKNOWLEDGE): Received message: " + textContent);

            // Simulate processing time
            Thread.sleep(200);

            // Manually acknowledge the message. This will only work if the session is CLIENT_ACKNOWLEDGE.
            // If an exception occurs AFTER acknowledge(), the message is still gone.
            // If an exception occurs BEFORE acknowledge(), the message will be redelivered.
            message.acknowledge();
            System.out.println("Consumer (clientAckQueue): Message '" + textContent + "' acknowledged successfully.");

        } catch (JMSException | InterruptedException e) {
            System.err.println("Consumer (clientAckQueue): Error processing or acknowledging message: " + e.getMessage());
            // In CLIENT_ACKNOWLEDGE mode, if an exception occurs before acknowledge(),
            // the message will be redelivered.
            // Do NOT call acknowledge() in the catch block if you want redelivery on error.
        }
    }

    // Listener for 'transactionalQueue' (default: SESSION_TRANSACTED, managed by JmsTransactionManager)
    // Demonstrates transactional consumer behavior: commit on success, rollback on exception.
    @JmsListener(destination = "transactionalQueue")
    public void receiveTransactionalMessage(String message) {
        System.out.println("Consumer (transactionalQueue - Transactional): Received message: " + message);
        // If the message content indicates a rollback scenario,
        // we'll throw an exception to simulate processing failure and force a rollback.
        if (message.contains("rollback")) {
            System.out.println("Consumer (transactionalQueue): Simulating processing failure to force rollback.");
            // This unchecked exception will cause the JMS transaction for this message to roll back.
            // The message will be redelivered by the broker according to its redelivery policy.
            throw new RuntimeException("Simulated processing failure for transactional message: " + message);
        }
        System.out.println("Consumer (transactionalQueue): Message processed successfully within transaction.");
    }

    // Listener for 'syncRequestQueue'. This consumer will process the message asynchronously.
    // The producer demonstrates *synchronous polling* for its own purposes.
    @JmsListener(destination = "syncRequestQueue")
    public void receiveSyncRequestMessage(String message) {
        System.out.println("Consumer (syncRequestQueue): Received sync request: " + message);
        // In a real request-reply scenario, this consumer might send a reply message
        // back to the JMSReplyTo destination on the incoming message.
    }
}
