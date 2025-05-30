package com.myapp.api.resource.contact;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ContactApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(ContactApplicationTest.class);

//    @Test
    void main_shouldStartApplicationWithoutException() {
        // Arrange
        String[] args = new String[]{};

        // Act & Assert
        assertDoesNotThrow(() -> ContactApplication.main(args));
        logger.info("Successfully tested main method invocation");
    }

    @Test
    void contextLoads() {
        // This test ensures the Spring application context loads successfully
        // No additional assertions needed; if the context fails to load, the test fails
        logger.info("Successfully verified Spring application context loading");
    }
}