package com.myapp.api.resource.contact.controller;

import com.myapp.api.resource.contact.model.Contact;
import com.myapp.api.resource.contact.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private Contact sampleContact;

    @BeforeEach
    void setUp() {
        // Initialize sample contact for tests
        sampleContact = new Contact();
        sampleContact.setId(1L);
        sampleContact.setName("John Doe");
        sampleContact.setEmail("john.doe@example.com");
    }

    @Test
    void getAllContacts_shouldReturnAllContacts() {
        // Arrange
        Map<Long, Contact> contacts = new HashMap<>();
        contacts.put(1L, sampleContact);
        when(contactService.getAll()).thenReturn(contacts);

        // Act
        ResponseEntity<Map<Long, Contact>> response = contactController.getAllContacts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contacts, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(contactService, times(1)).getAll();
    }

    @Test
    void getContactById_shouldReturnContact_whenFound() {
        // Arrange
        when(contactService.findById(1L)).thenReturn(sampleContact);

        // Act
        ResponseEntity<Contact> response = contactController.getContactById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleContact, response.getBody());
        verify(contactService, times(1)).findById(1L);
    }

    @Test
    void getContactById_shouldReturnNotFound_whenContactDoesNotExist() {
        // Arrange
        when(contactService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<Contact> response = contactController.getContactById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(contactService, times(1)).findById(1L);
    }

    @Test
    void createContact_shouldCreateAndReturnContact() {
        // Arrange
        when(contactService.add(any(Contact.class))).thenReturn(sampleContact);

        // Act
        ResponseEntity<Contact> response = contactController.createContact(sampleContact);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleContact, response.getBody());
        verify(contactService, times(1)).add(any(Contact.class));
    }

    @Test
    void updateContact_shouldUpdateAndReturnContact() {
        // Arrange
        Contact updatedContact = new Contact();
        updatedContact.setId(1L);
        updatedContact.setName("Jane Doe");
        updatedContact.setEmail("jane.doe@example.com");
        when(contactService.update(eq(1L), any(Contact.class))).thenReturn(updatedContact);

        // Act
        ResponseEntity<Contact> response = contactController.updateContact(1L, updatedContact);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedContact, response.getBody());
        assertEquals("Jane Doe", response.getBody().getName());
        verify(contactService, times(1)).update(eq(1L), any(Contact.class));
    }

    @Test
    void deleteContact_shouldDeleteAndReturnContact_whenFound() {
        // Arrange
        when(contactService.remove(1L)).thenReturn(sampleContact);

        // Act
        ResponseEntity<Contact> response = contactController.deleteContact(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleContact, response.getBody());
        verify(contactService, times(1)).remove(1L);
    }

    @Test
    void deleteContact_shouldReturnNotFound_whenContactDoesNotExist() {
        // Arrange
        when(contactService.remove(1L)).thenReturn(null);

        // Act
        ResponseEntity<Contact> response = contactController.deleteContact(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(contactService, times(1)).remove(1L);
    }
}