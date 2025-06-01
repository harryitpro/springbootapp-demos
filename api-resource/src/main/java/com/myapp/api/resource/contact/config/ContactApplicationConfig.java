package com.myapp.api.resource.contact.config;

import com.myapp.error.handling.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class for contact module.
 * Imports global exception handling configuration.
 */
@Configuration
@Import(GlobalExceptionHandler.class)
public class ContactApplicationConfig {
    // You may add other @Bean definitions here in the future
}
