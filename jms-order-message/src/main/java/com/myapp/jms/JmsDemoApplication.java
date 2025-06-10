// JmsDemoApplication.java
// This is the main Spring Boot application class.
// It configures all JMS aspects: JSON conversion, acknowledgment modes,
// persistence modes, local transactions, and sets up a CommandLineRunner
// to demonstrate various send/receive scenarios.

package com.myapp.jms;

import com.myapp.jms.model.Order;
import com.myapp.jms.service.JmsProducerService;
import jakarta.jms.ConnectionFactory; // Correct Jakarta EE JMS API import
import jakarta.jms.DeliveryMode; // Correct Jakarta EE JMS API import
import jakarta.jms.Session; // Correct Jakarta EE JMS API import
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate; // Correct JmsTemplate import
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJms // Enables Spring's JMS capabilities
@EnableTransactionManagement // Enables Spring's declarative transaction management (@Transactional)
public class JmsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmsDemoApplication.class, args);
    }

    // 1. Configure MessageConverter for JSON (Object <-> JSON String in TextMessage)
    //    This bean is automatically wired into JmsTemplate and DefaultJmsListenerContainerFactory
    //    by Spring Boot due to its type.
    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT); // Convert object to JSON String
        converter.setTypeIdPropertyName("_type"); // Add type info header for deserialization
        System.out.println("--- JmsDemoApplication: MappingJackson2MessageConverter configured ---");
        return converter;
    }

    // 2. Custom JmsListenerContainerFactory for CLIENT_ACKNOWLEDGE mode
    //    This factory needs to be explicitly named ("myClientAckFactory") so @JmsListener can reference it.
    @Bean
    public DefaultJmsListenerContainerFactory myClientAckFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory); // Apply Spring Boot's defaults (like message converter)

        // Customize the session for CLIENT_ACKNOWLEDGE mode
        factory.setSessionTransacted(false); // Must be false for non-transactional ack modes
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        System.out.println("--- JmsDemoApplication: 'myClientAckFactory' (CLIENT_ACKNOWLEDGE) configured ---");
        return factory;
    }

    // 3. Custom JmsTemplate bean
    //    This explicit bean definition overrides Spring Boot's auto-configured JmsTemplate.
    //    It ensures the specified default delivery mode and uses our custom MessageConverter.
    @Bean
    public JmsTemplate jmsTemplate(
            ConnectionFactory connectionFactory,
            MappingJackson2MessageConverter jacksonJmsMessageConverter) { // Inject the converter
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(jacksonJmsMessageConverter); // Set the JSON converter
        template.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // Set default delivery mode for this template
        // template.setExplicitQosEnabled(true); // Optional: Ensures set QoS are always used
        System.out.println("--- JmsDemoApplication: Custom JmsTemplate bean configured (NON_PERSISTENT default) ---");
        return template;
    }

    // 4. Local JMS Transaction Manager
    //    This enables @Transactional annotation for JMS operations within a local transaction scope.
    @Bean
    public PlatformTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        JmsTransactionManager transactionManager = new JmsTransactionManager();
        transactionManager.setConnectionFactory(connectionFactory);
        System.out.println("--- JmsDemoApplication: JmsTransactionManager configured ---");
        return transactionManager;
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
