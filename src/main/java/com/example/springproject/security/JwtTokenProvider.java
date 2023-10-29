package com.example.springproject.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secret;
    private long expirationInMs;

    public String generateToken(UserDetails userDetails){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);
        return Jwts.builder()
             //   .setSubject()
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

}
