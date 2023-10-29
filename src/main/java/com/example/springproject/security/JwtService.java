package com.example.springproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.*;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mysecretkey";
    private static final int KEY_LEN = 64;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails){
        return builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 + 60 * 24))
                .signWith(SignatureAlgorithm.HS512, secretAsKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return parser()
                .setSigningKey(secretAsKey())
                .parseClaimsJws(token)
                .getBody();
    }

//    public Claims decode(String jwt) {
//        SecretKey secretKey = new SecretKeySpec(secretAsKey(), "HmacSHA512");
//
//        Jws<Claims> claims = Jwts.parserBuilder()
//                .verifyWith(secretKey)
//                .decryptWith(secretKey)
//                .build()
//                .parseSignedClaims(jwt);
//
//        return claims.getPayload();
//    }

    public byte[] secretAsKey() {
        byte[] key = SECRET_KEY.getBytes();
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
