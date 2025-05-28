package com.myapp.service;

import com.myapp.model.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactServiceTest {

    private ContactService contactService;

    @BeforeEach
    void setUp() {
        contactService = new ContactService();
    }

    @Test
    void testAddContact() {
        Contact contact = new Contact();
        contact.setName("Alice");
        contact.setEmail("alice@example.com");

        Contact added = contactService.add(contact);

        assertThat(added.getId()).isNotNull();
        assertThat(contactService.findById(added.getId())).isEqualTo(added);
    }

    @Test
    void testFindByIdReturnsNullWhenNotFound() {
        Contact contact = contactService.findById(999L);
        assertThat(contact).isNull();
    }

    @Test
    void testGetAllReturnsCorrectCount() {
        contactService.add(new Contact("Bob", "bob@example.com"));
        contactService.add(new Contact("Carol", "carol@example.com"));

        Map<Long, Contact> all = contactService.getAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void testRemoveContact() {
        Contact contact = contactService.add(new Contact("Dave", "dave@example.com"));
        Long id = contact.getId();

        Contact removed = contactService.remove(id);

        assertThat(removed).isNotNull();
        assertThat(contactService.findById(id)).isNull();
    }

    @Test
    void testUpdateExistingContact() {
        Contact contact = contactService.add(new Contact("Eve", "eve@example.com"));
        Long id = contact.getId();

        Contact updated = new Contact("Eve Updated", "eve.new@example.com");
        Contact result = contactService.update(id, updated);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Eve Updated");
        assertThat(result.getEmail()).isEqualTo("eve.new@example.com");
    }

    @Test
    void testUpdateNonexistentContactReturnsNull() {
        Contact updated = new Contact("Ghost", "ghost@example.com");
        Contact result = contactService.update(999L, updated);

        assertThat(result).isNull();
    }
}
