package com.myapp.contact.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for contact module.
 * Imports global exception handling configuration.
 */
@Configuration
@ComponentScan(basePackages = {"com.myapp"})
public class ContactApplicationConfig {
    // You may add other @Bean definitions here in the future
}
