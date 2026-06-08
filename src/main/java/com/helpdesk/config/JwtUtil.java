package com.helpdesk.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final String SECRET =
            "dinesh-helpdesk-ai-jwt-security-key-for-springboot-project-2026\";";

    public static String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                    new Date(System.currentTimeMillis()
                    + 86400000)
                )
                .signWith(
                    Keys.hmacShaKeyFor(SECRET.getBytes()),
                    SignatureAlgorithm.HS256
                )
                .compact();
    }
        
    public static String extractEmail(String token) {

    return Jwts.parserBuilder()
            .setSigningKey(
                    Keys.hmacShaKeyFor(
                            SECRET.getBytes()
                    )
            )
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }       
}