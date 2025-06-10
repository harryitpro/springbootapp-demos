package com.myapp.jms.config;

import jakarta.jms.*;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJms // Enables Spring's JMS capabilities
@EnableTransactionManagement // Enables Spring's declarative transaction management (@Transactional)
public class JmsConfig {

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

    public Queue orderQueue() {
        return () -> "myQueue";
    }
}
