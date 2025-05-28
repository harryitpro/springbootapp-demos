package com.myapp.service;

import com.myapp.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final Map<Long, Contact> contactMap;
    private final AtomicLong idGenerator = new AtomicLong();

    public ContactService() {
        contactMap = new HashMap<>();
        logger.info("ContactService initialized with empty contact map.");
    }

    public Contact findById(Long id) {
        logger.info("Looking up contact with ID: {}", id);
        Contact contact = contactMap.get(id);
        if (contact != null) {
            logger.debug("Found contact: {}", contact);
        } else {
            logger.warn("No contact found with ID: {}", id);
        }
        return contact;
    }

    public Map<Long, Contact> getAll() {
        logger.info("Retrieving all contacts");
        logger.debug("Total contacts found: {}", contactMap.size());
        return contactMap;
    }

    public Contact add(Contact contact) {
        Long id = idGenerator.incrementAndGet();
        contact.setId(id);
        contactMap.put(id, contact);
        logger.info("Added new contact with ID: {}", id);
        logger.debug("Contact details: {}", contact);
        return contact;
    }

    public Contact remove(Long id) {
        logger.info("Removing contact with ID: {}", id);
        Contact removed = contactMap.remove(id);
        if (removed != null) {
            logger.info("Contact removed successfully");
            logger.debug("Removed contact: {}", removed);
        } else {
            logger.warn("No contact found to remove with ID: {}", id);
        }
        return removed;
    }

    public Contact update(Long id, Contact contact) {
        logger.info("Updating contact with ID: {}", id);
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
