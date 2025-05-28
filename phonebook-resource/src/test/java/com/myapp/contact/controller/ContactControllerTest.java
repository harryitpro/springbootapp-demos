package com.myapp.contact.controller;

import com.myapp.contact.model.Contact;
import com.myapp.contact.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ContactControllerTest.class);

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private Contact sampleContact;

    @BeforeEach
    void setUp() {
        // Initialize a sample contact for testing
        sampleContact = new Contact();
        sampleContact.setId(1L);
        sampleContact.setName("John Doe");
        sampleContact.setEmail("john.doe@example.com");
    }

    @Test
    void getAll_shouldReturnAllContacts() {
        // Arrange
        Map<Long, Contact> contacts = new HashMap<>();
        contacts.put(1L, sampleContact);
        when(contactService.getAll()).thenReturn(contacts);

        // Act
        Map<Long, Contact> result = contactController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleContact, result.get(1L));
        verify(contactService, times(1)).getAll();
        logger.info("Successfully tested getAll endpoint");
    }

    @Test
    void findById_shouldReturnContactWhenExists() {
        // Arrange
        Long id = 1L;
        when(contactService.findById(id)).thenReturn(sampleContact);

        // Act
        Contact result = contactController.findByName(id);

        // Assert
        assertNotNull(result);
        assertEquals(sampleContact, result);
        assertEquals("John Doe", result.getName());
        verify(contactService, times(1)).findById(id);
        logger.info("Successfully tested findById with existing ID: {}", id);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        // Arrange
        Long id = 2L;
        when(contactService.findById(id)).thenReturn(null);

        // Act
        Contact result = contactController.findByName(id);

        // Assert
        assertNull(result);
        verify(contactService, times(1)).findById(id);
        logger.info("Successfully tested findById with non-existent ID: {}", id);
    }

    @Test
    void add_shouldCreateAndReturnContact() {
        // Arrange
        Contact newContact = new Contact();
        newContact.setName("Jane Doe");
        newContact.setEmail("jane.doe@example.com");

        Contact createdContact = new Contact();
        createdContact.setId(2L);
        createdContact.setName("Jane Doe");
        createdContact.setEmail("jane.doe@example.com");

        when(contactService.add(newContact)).thenReturn(createdContact);

        // Act
        Contact result = contactController.add(newContact);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Jane Doe", result.getName());
        verify(contactService, times(1)).add(newContact);
        logger.info("Successfully tested add endpoint");
    }

    @Test
    void update_shouldUpdateAndReturnContact() {
        // Arrange
        Long id = 1L;
        Contact updatedContact = new Contact();
        updatedContact.setName("John Updated");
        updatedContact.setEmail("john.updated@example.com");

        Contact returnedContact = new Contact();
        returnedContact.setId(id);
        returnedContact.setName("John Updated");
        returnedContact.setEmail("john.updated@example.com");

        when(contactService.update(id, updatedContact)).thenReturn(returnedContact);

        // Act
        Contact result = contactController.update(id, updatedContact);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John Updated", result.getName());
        verify(contactService, times(1)).update(id, updatedContact);
        logger.info("Successfully tested update endpoint for ID: {}", id);
    }

    @Test
    void remove_shouldDeleteAndReturnContactWhenExists() {
        // Arrange
        Long id = 1L;
        when(contactService.remove(id)).thenReturn(sampleContact);

        // Act
        Contact result = contactController.remove(id);

        // Assert
        assertNotNull(result);
        assertEquals(sampleContact, result);
        verify(contactService, times(1)).remove(id);
        logger.info("Successfully tested remove endpoint for existing ID: {}", id);
    }

    @Test
    void remove_shouldReturnNullWhenNotFound() {
        // Arrange
        Long id = 2L;
        when(contactService.remove(id)).thenReturn(null);

        // Act
        Contact result = contactController.remove(id);

        // Assert
        assertNull(result);
        verify(contactService, times(1)).remove(id);
        logger.info("Successfully tested remove endpoint for non-existent ID: {}", id);
    }
}