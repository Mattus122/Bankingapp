package com.example.bankingapp.validation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.InvalidJwtToken;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    public class JwtDecoder {

        private static final String secretKEY = "c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl";

        private Key getSigninKey() {
            byte[] keyBytes = Decoders.BASE64.decode(secretKEY);
            return Keys.hmacShaKeyFor(keyBytes);
        }

        public Claims decodeJWT(String jwt) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        }

    }
    public boolean validateToken(String token){
        JwtDecoder jwtDecoder = new JwtDecoder();
        Claims claims = jwtDecoder.decodeJWT(token);
        String subject = claims.getSubject();
        Optional<User> findUser = userRepository.findByEmail(subject);
        if (findUser.isPresent()){
            return true;
        }
        else {
            throw new InvalidJwtToken("ERROR RESOURCE NOT FOUND");
        }

    }
//
//
//
////    public boolean validateToken(String token) {
////        JwtDecoder decoder = new JwtDecoder();
////        Claims claims = decoder.decodeJWT(token);
////        String subject = claims.getSubject();
////        return user.getEmail().equals(subject);
////
////        final String email = jwtService.getEmailFromToken(token);
////        return (email.equals(user.getEmail()) && !jwtService.isTokenExpired(token));
////        DecodedJWT decodedJWT = JWT.decode(token);
////        Date expiresAt = decodedJWT.getExpiresAt();
////        Claims claims = decoder.decodeJWT(token);
////    }
//
//        public boolean validateToken(String token) {
//
//
//        DecodedJWT decodedJWT;
//        try {
//            decodedJWT = JWT.decode(token);
//
//        } catch (JWTDecodeException e) {
//            throw new JWTDecodeException("Unable to Decode the required Jwt token");
//        }
//        Date expiresAt = decodedJWT.getExpiresAt();
//
//        return expiresAt.before(new Date());
//
//    }

}
