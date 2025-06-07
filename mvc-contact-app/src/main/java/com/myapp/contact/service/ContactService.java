package com.myapp.contact.service;


import com.myapp.contact.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service for managing contact data in memory.
 */
@Service
public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final Map<Long, Contact> contactMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @PostConstruct
    public void init() {
        logger.info("ContactService initialized with empty contact map.");
    }

    /**
     * Find a contact by its ID.
     *
     * @param id the contact ID
     * @return the contact if found, or null
     */
    public Contact findById(Long id) {
        logger.debug("Looking up contact with ID: {}", id);
        Contact contact = contactMap.get(id);
        if (contact == null) {
            logger.warn("No contact found with ID: {}", id);
        }
        return contact;
    }

    /**
     * Retrieve all contacts.
     *
     * @return a map of all contacts
     */
    public Map<Long, Contact> getAll() {
        logger.debug("Retrieving all contacts (count: {})", contactMap.size());
        return contactMap;
    }

    /**
     * Add a new contact.
     *
     * @param contact the contact to add
     * @return the added contact with ID assigned
     */
    public Contact add(Contact contact) {
        Long id = idGenerator.incrementAndGet();
        contact.setId(id);
        contactMap.put(id, contact);
        logger.info("Added new contact with ID: {}", id);
        logger.debug("Contact details: {}", contact);
        return contact;
    }

    /**
     * Remove a contact by ID.
     *
     * @param id the contact ID
     * @return the removed contact, or null if not found
     */
    public Contact remove(Long id) {
        logger.debug("Removing contact with ID: {}", id);
        Contact removed = contactMap.remove(id);
        if (removed != null) {
            logger.info("Contact removed successfully");
        } else {
            logger.warn("No contact found to remove with ID: {}", id);
        }
        return removed;
    }

    /**
     * Update a contact by ID.
     *
     * @param id      the contact ID
     * @param contact the updated contact data
     * @return the updated contact, or null if not found
     */
    public Contact update(Long id, Contact contact) {
        logger.debug("Updating contact with ID: {}", id);
        if (contactMap.containsKey(id)) {
            contact.setId(id);
            contactMap.put(id, contact);
            logger.info("Contact updated successfully");
            logger.debug("Updated contact: {}", contact);
            return contact;
        } else {
            logger.warn("No contact found with ID: {}, update failed", id);
            return null;
        }
    }
}
