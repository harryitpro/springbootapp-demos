package com.myapp.poc.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthDemoController {
    @GetMapping("/public")
    public String getPublicResource() {
        return "public resource returned.";
    }

    @GetMapping("/private")
    public String getPrivateResource() {
        return "private resource returned.";
    }

}
