package com.myapp.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContactApplication {
    private static final Logger logger = LoggerFactory.getLogger(ContactApplication.class);

    public static void main(String[] args) {
        logger.info("Starting PhonebookApplication...");
        SpringApplication.run(ContactApplication.class, args);
        logger.info("PhonebookApplication started successfully.");
    }
}
