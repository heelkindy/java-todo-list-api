package com.example.javatodolistapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // 1. Generate JWT
    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // Get username from object authentication

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(this.key())
                .compact();
    }

    // 3. Get username from JWT
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 4. Validate JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.key())
                    .build()
                    .parse(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
