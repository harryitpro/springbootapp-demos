package com.myapp.poc.auth.defaultauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DefaultAuthDemoController {
    @GetMapping
    public String handlePublicResource() {
        return "handle GET request";
    }
}
