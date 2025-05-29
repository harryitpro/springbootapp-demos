package com.myapp.api.resource.contact.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;

    public Contact() {
    }

    public Contact(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
