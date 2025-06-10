// JmsDemoApplication.java
// This is the main Spring Boot application class.
// It configures all JMS aspects: JSON conversion, acknowledgment modes,
// persistence modes, local transactions, and sets up a CommandLineRunner
// to demonstrate various send/receive scenarios.

package com.myapp.jms;

import com.myapp.jms.model.Order;
import com.myapp.jms.service.JmsProducerService;
// Correct Jakarta EE JMS API import
import jakarta.jms.DeliveryMode; // Correct Jakarta EE JMS API import
// Correct Jakarta EE JMS API import
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
// Correct JmsTemplate import


@SpringBootApplication
public class JmsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmsDemoApplication.class, args);
    }

    // CommandLineRunner to execute messaging scenarios on application startup
    @Bean
    public CommandLineRunner run(JmsProducerService jmsProducerService) {
        return args -> {
            System.out.println("\n--- Application Started. Initiating Messaging Scenarios ---");

            // Scenario 1: Basic Plain Text Message (uses JmsTemplate's default NON_PERSISTENT)
            System.out.println("\n--- Scenario 1: Sending Basic Plain Text (Default NON_PERSISTENT) ---");
            jmsProducerService.sendPlainText("myQueue", "Hello from Spring Boot JMS Producer (Plain Text)!");

            // Scenario 2: JSON Object Message (uses JmsTemplate's default NON_PERSISTENT)
            System.out.println("\n--- Scenario 2: Sending JSON Order Object (Default NON_PERSISTENT) ---");
            Order newOrder = new Order("ORDER-1001", "Gaming PC", 1, 1500.00);
            jmsProducerService.sendOrderMessage("orderQueue", newOrder);

            // Scenario 3: Message for CLIENT_ACKNOWLEDGE Listener (uses JmsTemplate's default NON_PERSISTENT)
            System.out.println("\n--- Scenario 3: Sending Message for CLIENT_ACKNOWLEDGE Listener ---");
            jmsProducerService.sendPlainText("clientAckQueue", "Message for client acknowledgment!");

            // Scenario 4: Message with Explicit PERSISTENT Delivery Mode
            System.out.println("\n--- Scenario 4: Sending Message with Explicit PERSISTENT Delivery Mode ---");
            jmsProducerService.sendMessageWithQoS("myQueue", "This message is PERSISTENT!",
                    DeliveryMode.PERSISTENT, 9, 60000L); // High priority, 1 min TTL

            // Scenario 5: Message with Explicit NON_PERSISTENT Delivery Mode and short TTL
            System.out.println("\n--- Scenario 5: Sending Message with Explicit NON_PERSISTENT (short TTL) ---");
            jmsProducerService.sendMessageWithQoS("myQueue", "This message is NON_PERSISTENT with short TTL!",
                    DeliveryMode.NON_PERSISTENT, 0, 5000L); // Low priority, 5 sec TTL

            // Scenario 6: Transactional Send - Success
            System.out.println("\n--- Scenario 6: Transactional Send (Commit) ---");
            try {
                jmsProducerService.sendTransactionalMessage("transactionalQueue", "Transaction will commit.", false);
            } catch (Exception e) {
                System.err.println("JmsDemoApplication: Error in transactional send (expected commit, but caught exception): " + e.getMessage());
            }

            // Scenario 7: Transactional Send - Rollback
            System.out.println("\n--- Scenario 7: Transactional Send (Rollback) ---");
            try {
                jmsProducerService.sendTransactionalMessage("transactionalQueue", "Transaction will rollback!", true);
            } catch (Exception e) {
                System.err.println("JmsDemoApplication: Successfully caught transactional rollback: " + e.getMessage());
            }

            // Scenario 8: Synchronous Receive
            System.out.println("\n--- Scenario 8: Synchronous Receive ---");
            String syncRequestQueue = "syncRequestQueue";
            // Ensure a message is sent to the queue *before* attempting synchronous receive
            jmsProducerService.sendPlainText(syncRequestQueue, "Request for synchronous reply!");

            String receivedSyncMessage = jmsProducerService.receiveSynchronousMessage(syncRequestQueue, 5000L);
            System.out.println("--- Main App: Synchronously received: '" + receivedSyncMessage + "' ---");

            // Example of a synchronous receive that times out (will return null)
            String noMessage = jmsProducerService.receiveSynchronousMessage("emptyQueue", 2000L);
            System.out.println("--- Main App: Received from emptyQueue (expected null): '" + noMessage + "' ---");


            System.out.println("\n--- All Messaging Scenarios Initiated. Monitor Console for Consumer Output ---");
        };
    }
}
