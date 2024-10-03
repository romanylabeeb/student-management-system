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

    private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=";

    // get the key as a byte array
    private SecretKey getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // generate JWT Token
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
                //Todo make expiration_time as env
                .setExpiration(new Date(System.currentTimeMillis() + 50 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET);
        return builder.compact();
    }

    // validate the token
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // extract role from token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role").toString();
    }

    // extract userId from token
    public long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).get("id").toString());
    }

    // extract claims from the token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey()) // use the same secret key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
