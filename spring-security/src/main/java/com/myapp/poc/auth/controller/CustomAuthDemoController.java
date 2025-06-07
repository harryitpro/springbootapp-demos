package com.myapp.poc.auth.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("custom")
@RestController
@RequestMapping("/api")
public class CustomAuthDemoController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public API - No login required!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "User API - Welcome, your name is " + authentication.getName() + " you are authenticated user!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "Admin API - Welcome, admin!";
    }

    @GetMapping("/user-or-admin")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userOrAdminEndpoint() {
        return "User or Admin API - Welcome!";
    }
}
