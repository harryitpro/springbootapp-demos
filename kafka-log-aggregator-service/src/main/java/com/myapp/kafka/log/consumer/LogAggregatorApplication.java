package com.myapp.kafka.log.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogAggregatorApplication.class, args);
    }
}
