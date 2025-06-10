package com.myapp.jms.model;

import lombok.Data;

@Data
public class MessagePayload {
    private String id;
    private String content;
}