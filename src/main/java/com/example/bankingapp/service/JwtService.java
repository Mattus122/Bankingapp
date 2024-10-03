package com.example.bankingapp.service;

import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.UnauthorizedAccessException;
import com.example.bankingapp.exception.UserNotFoundException;
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
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;
    public String generateToken(JwtTokenDTO jwtTokenDTO) {
        User user = userRepository.findByEmail(jwtTokenDTO.getEmail())
                .orElseThrow(()->new UserNotFoundException("User not found with email: "+jwtTokenDTO.getEmail()));
        if(user.getPassword().equals(jwtTokenDTO.getPassword())){
            return Jwts.builder()
                    .setSubject(user.getFirstName())
                    .claim("userId",user.getId().toString())
                    .claim("email",user.getEmail())
                    .claim("lastName",user.getLastName())
                    .claim("age",user.getAge())
                    .claim("organisationName",user.getOrganisationName().toString())
                    .claim("role",user.getRole().toString())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                    .signWith(getSigninKey(),SignatureAlgorithm.HS256)
                    .compact();
        }
        else{
            throw new UnauthorizedAccessException("No User Found check the email and password");
        }
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode("c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl");
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String getEmailFromToken(String token) {return extractAllClaims(token).get("email", String.class);}
    public String returnOrganisationName(String token){return extractAllClaims(token).get("organisationName",String.class);}
    public String returnRole(String token){
        return extractAllClaims(token).get("role",String.class);
    }
    public String returnUserId(String token){return extractAllClaims(token).get("userId",String.class);}
}
