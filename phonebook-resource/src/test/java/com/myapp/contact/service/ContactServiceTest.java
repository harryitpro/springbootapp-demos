package com.myapp.contact.service;

import com.myapp.contact.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContactServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ContactServiceTest.class);

    private ContactService contactService;
    private Contact sampleContact;

    @BeforeEach
    void setUp() {
        // Initialize the service and sample contact before each test
        contactService = new ContactService();
        sampleContact = new Contact();
        sampleContact.setName("John Doe");
        sampleContact.setEmail("john.doe@example.com");
    }

    @Test
    void findById_shouldReturnContactWhenExists() {
        // Arrange
        Long id = contactService.add(sampleContact).getId();

        // Act
        Contact result = contactService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(sampleContact.getName(), result.getName());
        assertEquals(sampleContact.getEmail(), result.getEmail());
        logger.info("Successfully tested findById with existing ID: {}", id);
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        Contact result = contactService.findById(nonExistentId);

        // Assert
        assertNull(result);
        logger.info("Successfully tested findById with non-existent ID: {}", nonExistentId);
    }

    @Test
    void getAll_shouldReturnEmptyMapWhenNoContacts() {
        // Act
        Map<Long, Contact> result = contactService.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        logger.info("Successfully tested getAll with empty contact map");
    }

    @Test
    void getAll_shouldReturnAllContactsWhenPresent() {
        // Arrange
        contactService.add(sampleContact);
        Contact anotherContact = new Contact();
        anotherContact.setName("Jane Doe");
        anotherContact.setEmail("jane.doe@example.com");
        contactService.add(anotherContact);

        // Act
        Map<Long, Contact> result = contactService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        logger.info("Successfully tested getAll with {} contacts", result.size());
    }

    @Test
    void add_shouldCreateNewContactWithGeneratedId() {
        // Act
        Contact result = contactService.add(sampleContact);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(sampleContact.getName(), result.getName());
        assertEquals(sampleContact.getEmail(), result.getEmail());
        assertTrue(contactService.getAll().containsValue(result));
        logger.info("Successfully tested add with generated ID: {}", result.getId());
    }

    @Test
    void add_shouldHandleNullContact() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> contactService.add(null));
        logger.info("Successfully tested add with null contact");
    }

    @Test
    void remove_shouldDeleteAndReturnContactWhenExists() {
        // Arrange
        Long id = contactService.add(sampleContact).getId();

        // Act
        Contact result = contactService.remove(id);

        // Assert
        assertNotNull(result);
        assertEquals(sampleContact.getName(), result.getName());
        assertNull(contactService.findById(id));
        logger.info("Successfully tested remove with existing ID: {}", id);
    }

    @Test
    void remove_shouldReturnNullWhenNotFound() {
        // Arrange
        Long nonExistentId = 999L;

        // Act
        Contact result = contactService.remove(nonExistentId);

        // Assert
        assertNull(result);
        logger.info("Successfully tested remove with non-existent ID: {}", nonExistentId);
    }

    @Test
    void update_shouldUpdateAndReturnContactWhenExists() {
        // Arrange
        Long id = contactService.add(sampleContact).getId();
        Contact updatedContact = new Contact();
        updatedContact.setName("John Updated");
        updatedContact.setEmail("john.updated@example.com");

        // Act
        Contact result = contactService.update(id, updatedContact);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());
        logger.info("Successfully tested update with existing ID: {}", id);
    }

    @Test
    void update_shouldReturnNullWhenNotFound() {
        // Arrange
        Long nonExistentId = 999L;
        Contact updatedContact = new Contact();
        updatedContact.setName("John Updated");

        // Act
        Contact result = contactService.update(nonExistentId, updatedContact);

        // Assert
        assertNull(result);
        logger.info("Successfully tested update with non-existent ID: {}", nonExistentId);
    }

    @Test
    void update_shouldHandleNullContact() {
        // Arrange
        Long id = contactService.add(sampleContact).getId();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> contactService.update(id, null));
        logger.info("Successfully tested update with null contact for ID: {}", id);
    }
}