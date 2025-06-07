package com.myapp.contact.controller;

import com.myapp.contact.model.Contact;
import com.myapp.contact.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
@Validated
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Fetch all contacts.
     */
    @GetMapping
    public ResponseEntity<Map<Long, Contact>> getAllContacts() {
        logger.info("Fetching all contacts");
        Map<Long, Contact> contacts = contactService.getAll();
        logger.debug("Found {} contacts", contacts.size());
        return ResponseEntity.ok(contacts);
    }

    /**
     * Fetch a contact by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        logger.info("Fetching contact with ID: {}", id);
        Contact contact = contactService.findById(id);
        if (contact != null) {
            logger.debug("Found contact: {}", contact);
            return ResponseEntity.ok(contact);
        } else {
            logger.warn("No contact found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Create a new contact.
     */
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contactDto) {
        logger.info("Creating new contact");
        logger.debug("Contact data: {}", contactDto);
        Contact created = contactService.add(contactDto);
        logger.info("Contact created with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing contact.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        logger.info("Updating contact with ID: {}", id);
        logger.debug("Updated contact data: {}", contact);
        Contact updated = contactService.update(id, contact);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a contact by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Contact> deleteContact(@PathVariable Long id) {
        logger.info("Deleting contact with ID: {}", id);
        Contact removed = contactService.remove(id);
        if (removed != null) {
            logger.info("Contact deleted successfully");
            return ResponseEntity.ok(removed);
        } else {
            logger.warn("No contact found to delete with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
