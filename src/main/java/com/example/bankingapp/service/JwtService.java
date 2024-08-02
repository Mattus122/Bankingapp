package com.example.bankingapp.service;

import com.example.bankingapp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private static final String secretKEY ="c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl";


    public String generateToken(User user) {
        return Jwts.builder().setSubject(user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigninKey() {
        byte[] Key = Decoders.BASE64.decode(secretKEY);
        return Keys.hmacShaKeyFor(Key);
    }
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigninKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }

//    public String getEmailFromToken(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    public String getFirstNameFromToken(String token) {
//        return extractAllClaims(token).get("firstName", String.class);
//    }
//
//    public String getLastNameFromToken(String token) {
//        return extractAllClaims(token).get("lastName", String.class);
//    }
//
//    public boolean isTokenExpired(String token) {
//        Date expiration = extractAllClaims(token).getExpiration();
//        return expiration.before(new Date());
//    }
//
//    public Date getIssuedAtDate(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.getIssuedAt();
//    }
//
//    public Date getExpirationDate(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.getExpiration();
//    }

}
