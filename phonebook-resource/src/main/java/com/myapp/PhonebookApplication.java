package com.myapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PhonebookApplication {
    private static final Logger logger = LoggerFactory.getLogger(PhonebookApplication.class);

    public static void main(String[] args) {
        logger.info("Starting PhonebookApplication...");
        SpringApplication.run(PhonebookApplication.class, args);
        logger.info("PhonebookApplication started successfully.");
    }
}
