package com.myapp.common.utilities.controller;

import com.myapp.common.utilities.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/utils")
public class UtilsDemoController {

    @PostMapping("/jwt/generate")
    public Map<String, String> generateToken(@RequestParam String username) {
        String token = JwtUtil.generateToken(username, List.of("ROLE_USER"));
        return Map.of("token", token);
    }

    @PostMapping("/jwt/parse")
    public Map<String, Object> parseToken(@RequestParam String token) {
        Jws<Claims> jws = JwtUtil.parseToken(token);
        Claims claims = jws.getBody();
        return Map.of(
                "subject", claims.getSubject(),
                "roles", claims.get("roles"),
                "issuedAt", claims.getIssuedAt(),
                "expiration", claims.getExpiration()
        );
    }
}

