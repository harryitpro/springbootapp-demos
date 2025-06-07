package com.myapp.resourceserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ResourceController {

    @GetMapping("/data")
    public Map<String, String> getData(@AuthenticationPrincipal Jwt jwt) {
        // @AuthenticationPrincipal injects the decoded JWT
        // You can access claims from the token like this:
        return Map.of(
                "message", "This is protected data!",
                "client-id", jwt.getSubject(), // "sub" claim is the client_id for client_credentials
                "scopes", jwt.getClaimAsString("scope")
        );
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint.";
    }

    @GetMapping("/protected")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public String protectedEndpoint() {
        return "This is a protected endpoint. You have 'read' scope.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public String adminEndpoint() {
        return "This is an admin endpoint. You have 'admin' scope.";
    }
}