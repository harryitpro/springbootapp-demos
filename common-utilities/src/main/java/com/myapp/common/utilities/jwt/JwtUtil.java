package com.myapp.common.utilities.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    private static final Key SECRET_KEY_HS256 = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Key SECRET_KEY_RS256 = Keys.secretKeyFor(SignatureAlgorithm.RS256);
    private static final long EXPIRATION_MS = 3600000; // 1 hour

    public static String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY_HS256)
                .compact();
    }

    public static Jws<Claims> parseToken_hs256(String token) {
        return parseToken(token,SECRET_KEY_HS256);
    }

    public static Jws<Claims> parseToken(String token,Key secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    public static Jws<Claims> parseToken_rs256(String token) {
        return parseToken(token,SECRET_KEY_RS256);
    }

}

