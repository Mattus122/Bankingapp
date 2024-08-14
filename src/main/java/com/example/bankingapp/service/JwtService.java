package com.example.bankingapp.service;

import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
import com.example.bankingapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;
    private static final String secretKEY = "c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl";

    public String generateToken(JwtTokenDTO jwtTokenDTO) {
        Optional<User> userOptional = userRepository.findByEmail(jwtTokenDTO.getEmail());
        if(userOptional.isPresent()&& userOptional.get().getPassword().equals(jwtTokenDTO.getPassword())){
            return Jwts.builder()
                    .setSubject(userOptional.get().getRole().toString())
                    .claim("email" , userOptional.get().getEmail())
                    .claim("password" , userOptional.get().getPassword())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                    .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                    .compact();
        }
        else{
            throw new UserNotFoundExcetion("No User Found check the email and password");
        }
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
}
