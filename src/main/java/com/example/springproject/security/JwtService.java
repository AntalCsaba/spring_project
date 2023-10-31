package com.example.springproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.parser;

@Service
public class JwtService {

    @Value("${secret_key}")
    private String secret;
    private static final int KEY_LEN = 64;

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> claims,
                                UserDetails userDetails) {

        Key secretKey = new SecretKeySpec(secretAsKey(), "HmacSHA512");


        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .signWith(secretKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        SecretKey secretKey = new SecretKeySpec(secretAsKey(), "HmacSHA512");
        return parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

    }

    public byte[] secretAsKey() {
        byte[] key = secret.getBytes();
        byte[] result = new byte[KEY_LEN];
        int p = 0;
        while (p < key.length) {
            int l = Math.min(key.length, result.length - p);
            System.arraycopy(key, 0, result, p, l);
            p += l;
        }

        return result;
    }

}
