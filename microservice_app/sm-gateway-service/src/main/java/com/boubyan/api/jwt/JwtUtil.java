package com.boubyan.api.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Use a strong secret key (keep this secret and do not hardcode in production)
    private static final String SECRET = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=";

    // Get the key as a byte array
    private SecretKey getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // Validate the token
    public void validateToken(String token) {
        Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token);
    }

}
