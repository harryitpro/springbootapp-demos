package com.myapp.api.resource.contact.controller;

import com.myapp.api.resource.contact.model.Contact;
import com.myapp.api.resource.contact.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Map<Long, Contact> getAll() {
        logger.info("Fetching all contacts");
        Map<Long, Contact> contacts = contactService.getAll();
        logger.debug("Found {} contacts", contacts.size());
        return contacts;
    }

    @GetMapping("/{id}")
    public Contact findByName(@PathVariable("id") Long id) {
        logger.info("Fetching contact with ID: {}", id);
        Contact contact = contactService.findById(id);
        if (contact != null) {
            logger.debug("Found contact: {}", contact);
        } else {
            logger.warn("No contact found with ID: {}", id);
        }
        return contact;
    }

    @PostMapping
    public Contact add(@RequestBody Contact contactDto) {
        logger.info("Adding new contact");
        logger.debug("Contact details: {}", contactDto);
        Contact created = contactService.add(contactDto);
        logger.info("Contact added with ID: {}", created.getId());
        return created;
    }

    @PutMapping("/{id}")
    public Contact update(@PathVariable Long id, @RequestBody Contact contact) {
        logger.info("Updating contact with ID: {}", id);
        logger.debug("New contact data: {}", contact);
        Contact updated = contactService.update(id, contact);
        logger.info("Contact updated successfully for ID: {}", id);
        return updated;
    }

    @DeleteMapping("/{id}")
    public Contact remove(@PathVariable Long id) {
        logger.info("Deleting contact with ID: {}", id);
        Contact removed = contactService.remove(id);
        if (removed != null) {
            logger.info("Contact deleted successfully");
        } else {
            logger.warn("No contact found to delete with ID: {}", id);
        }
        return removed;
    }
}
