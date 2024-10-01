package com.boubyan.api.jwt;


import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtUtil {

    // Use a strong secret key (keep this secret and do not hardcode in production)
    private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=";

    // Get the key as a byte array
    private SecretKey getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // Generate JWT Token
    public String generateToken(String username, String role, long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("id", userId);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 50 * 60 * 1000)) // Token expires in 5 minutes
                .signWith(SignatureAlgorithm.HS256, SECRET);
//                .signWith(getKey()); // Use the secret key here
        return builder.compact();
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract role from token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role").toString();
    }

    // Extract userId from token
    public long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).get("id").toString());
    }

    // Extract claims from the token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey()) // Use the same secret key for verification
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {

        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
