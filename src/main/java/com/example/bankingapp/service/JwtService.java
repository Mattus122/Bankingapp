package com.example.bankingapp.service;

import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {
    private static final String secretKEY = "c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl";

    public String generateToken(JwtTokenDTO jwtTokenDTO) {
        return Jwts.builder()
                .setSubject(jwtTokenDTO.getRole().toString())
                .claim("email" , jwtTokenDTO.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }




//    public String extractStringEmail(String token) {
//        return extractClaims(token  , Claims::getSubject);
//    }
//    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);//we are using this to get a particular Claim
//        return claimsResolver.apply(claims);
//    }
//
//    public Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigninKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }


}


//@Service
//public class JwtService {
//    private static final String secretKEY ="kI8jWFcSiLnsAjyuyYMg1U7fUwYr9JrZ1n/aVlKbh6I=";
//
//
//    public String generateToken(User user) {
//        return Jwts.builder().setSubject(user.getEmail())
//                .claim("firstName", user.getFirstName())
//                .claim("lastName", user.getLastName())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
//                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key getSigninKey() {
//        byte[] Key = Decoders.BASE64.decode(secretKEY);
//        return Keys.hmacShaKeyFor(Key);
//    }
////    private Claims extractAllClaims(String token) {
////        return Jwts.parserBuilder()
////                .setSigningKey(getSigninKey())
////                .build()
////                .parseClaimsJws(token)
////                .getBody();
////    }
//
////    public String getEmailFromToken(String token) {
////        return extractAllClaims(token).getSubject();
////    }
////
////    public String getFirstNameFromToken(String token) {
////        return extractAllClaims(token).get("firstName", String.class);
////    }
////
////    public String getLastNameFromToken(String token) {
////        return extractAllClaims(token).get("lastName", String.class);
////    }
////
////    public boolean isTokenExpired(String token) {
////        Date expiration = extractAllClaims(token).getExpiration();
////        return expiration.before(new Date());
////    }
////
////    public Date getIssuedAtDate(String token) {
////        Claims claims = extractAllClaims(token);
////        return claims.getIssuedAt();
////    }
////
////    public Date getExpirationDate(String token) {
////        Claims claims = extractAllClaims(token);
////        return claims.getExpiration();
////    }
//
//}
