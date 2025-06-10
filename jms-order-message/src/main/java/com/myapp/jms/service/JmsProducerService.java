// JmsProducerService.java
// This service contains methods for sending messages with different configurations
// and a method for synchronous receiving.

package com.myapp.jms.service;

import com.myapp.jms.model.Order;
import jakarta.jms.DeliveryMode; // Correct Jakarta EE JMS API import
import jakarta.jms.JMSException; // Correct Jakarta EE JMS API import
import jakarta.jms.Message; // Correct Jakarta EE JMS API import
import jakarta.jms.Session; // Correct Jakarta EE JMS API import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
// Removed: import org.springframework.jms.core.MessageCreator; // No longer needed for this approach
import org.springframework.jms.support.converter.MessageConverter; // Correct MessageConverter import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JmsProducerService {

    // JmsTemplate will be auto-wired with the custom JmsTemplate bean defined in JmsDemoApplication
    @Autowired
    private JmsTemplate jmsTemplate;

    // Inject the MessageConverter directly to use it for explicit conversion if needed,
    // though convertAndSend handles it implicitly when MessagePostProcessor is used.
    @Autowired
    private MessageConverter messageConverter;

    // Sends a plain text message (will use JmsTemplate's default delivery mode)
    public void sendPlainText(String destinationName, String messageContent) {
        System.out.println("ProducerService: Sending plain text to " + destinationName + ": " + messageContent);
        jmsTemplate.convertAndSend(destinationName, messageContent);
    }

    // Sends an Order object (will use JmsTemplate's default delivery mode and JSON MessageConverter)
    public void sendOrderMessage(String destinationName, Order order) {
        System.out.println("ProducerService: Sending Order object to " + destinationName + ": " + order);
        jmsTemplate.convertAndSend(destinationName, order);
    }

    /**
     * Sends a message with explicit Quality of Service (QoS) parameters (DeliveryMode, Priority, TimeToLive).
     * This method uses jmsTemplate.convertAndSend() with a MessagePostProcessor
     * to set the QoS properties directly on the JMS Message before it's sent.
     *
     * @param destinationName The name of the queue or topic.
     * @param messageContent The content of the message (can be a String or a POJO like Order).
     * @param deliveryMode The persistence mode (PERSISTENT or NON_PERSISTENT).
     * @param priority The message priority (0-9).
     * @param timeToLive The message's time-to-live in milliseconds.
     */
    public void sendMessageWithQoS(String destinationName, Object messageContent,
                                   int deliveryMode, int priority, long timeToLive) {
        System.out.println("ProducerService: Sending to " + destinationName + " (QoS: " +
                (deliveryMode == DeliveryMode.PERSISTENT ? "PERSISTENT" : "NON_PERSISTENT") +
                ", Priority: " + priority + ", TTL: " + timeToLive + "ms): " + messageContent);

        // CORRECTED: Use convertAndSend with MessagePostProcessor to apply QoS directly
        // This is a more common and robust way to set QoS on a per-message basis
        // while still leveraging the default MessageConverter.
        jmsTemplate.convertAndSend(destinationName, messageContent, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                // Set the DeliveryMode, Priority, and TimeToLive directly on the JMS Message object
                // within the post-processing step, after the content has been converted.
                message.setJMSDeliveryMode(deliveryMode);
                message.setJMSPriority(priority);
                if (timeToLive > 0) {
                    message.setJMSExpiration(System.currentTimeMillis() + timeToLive);
                } else {
                    message.setJMSExpiration(0); // 0 means never expire
                }
                // Optional: set custom headers/properties here if needed
                // message.setStringProperty("CustomProperty", "CustomValue");
                return message;
            }
        });
    }

    // Sends a message within a local JMS transaction
    // The "jmsTransactionManager" qualifier links this to the JmsTransactionManager bean.
    @Transactional("jmsTransactionManager")
    public void sendTransactionalMessage(String destinationName, String messageContent, boolean throwError) {
        System.out.println("ProducerService (Transactional Method): Sending message to " + destinationName + ": " + messageContent);
        jmsTemplate.convertAndSend(destinationName, messageContent);

        if (throwError) {
            // Simulate an error that causes a transaction rollback
            throw new RuntimeException("Simulated transactional error for " + destinationName);
        }
        System.out.println("ProducerService (Transactional Method): Message sent within transaction. Committing...");
    }

    // Performs a synchronous message receive operation with a specific timeout
    public String receiveSynchronousMessage(String destinationName, long timeout) {
        // This method needs to temporarily set the receiveTimeout on the JmsTemplate.
        // For a shared JmsTemplate bean, setting this dynamically is not thread-safe if
        // multiple threads call this concurrently. In a real-world multi-threaded scenario
        // for synchronous receives, you might consider:
        // 1. Using a dedicated JmsTemplate instance for synchronous receives.
        // 2. Using a MessageConsumer.receive() with manual resource management (more complex).
        // For this demo, assuming sequential calls, it's acceptable.

        long originalTimeout = jmsTemplate.getReceiveTimeout(); // Store original timeout
        try {
            jmsTemplate.setReceiveTimeout(timeout); // Set the specific timeout for this receive operation
            System.out.println("ProducerService (Synchronous Receiver): Polling " + destinationName + " for " + timeout + "ms...");
            // Call receiveAndConvert without the timeout argument, as the timeout is now set on the template
            Object receivedObject = jmsTemplate.receiveAndConvert(destinationName);

            if (receivedObject != null) {
                System.out.println("ProducerService (Synchronous Receiver): Received object: " + receivedObject);
                return receivedObject.toString();
            } else {
                System.out.println("ProducerService (Synchronous Receiver): No message received from " + destinationName + " within timeout.");
                return null;
            }
        } finally {
            // Always reset the timeout to its original value after the operation
            jmsTemplate.setReceiveTimeout(originalTimeout);
        }
    }
}
