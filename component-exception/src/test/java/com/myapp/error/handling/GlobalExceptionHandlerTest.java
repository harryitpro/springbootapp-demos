package com.myapp.error.handling;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBadRequest_ShouldReturnBadRequestResponse() {
        // Arrange
        String errorMessage = "Invalid argument provided";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleBadRequest(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }
}
