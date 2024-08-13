package com.example.bankingapp.validation;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.jwtExcetion.ForbiddenRequestException;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;


@Slf4j
@RequiredArgsConstructor
@Service
public class ValidationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigninKey() {
        // Use the same key as in JwtService
        byte[] keyBytes = Decoders.BASE64.decode("c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl");
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String getEmailFromToken(String token) {
        return extractAllClaims(token).get("email", String.class);
    }
    public String getSubjectFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }
    public String returnRole(String token){
        return getSubjectFromToken(token);
    }

//    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);//we are using this to get a particular Claim
//        return claimsResolver.apply(claims);
//    }


//    public boolean validateRole(String token, String requestType) {
//        String HARDCODED_TOKEN = token;
//        // Hardcoded token comparison
//        if (!token.equals(HARDCODED_TOKEN)) {
//            throw new ForbiddenRequestException("Invalid Token");
//        }
//
//        Claims claims = extractAllClaims(token);
//        String role = claims.getSubject();// Ensure "role" is a custom claim in your JWT
//
//        if ("ADMIN".equals(role) && ("POST".equals(requestType) || "PUT".equals(requestType) || "DELETE".equals(requestType))) {
//            return true;
//        } else {
//            throw new ForbiddenRequestException("Role does not match required permissions");
//        }
//    }


    public boolean isTokenExpired(String token) {
                Claims claims = extractAllClaims(token);
                Date expiration = claims.getExpiration();
                return expiration.before(new Date());

    }
    public boolean validateToken(String token , String requestType) {
        final String HARDCODED_TOKEN =token; // Use your actual hardcoded token

        // Token comparison
        if (!token.equals(HARDCODED_TOKEN)) {
            throw new ForbiddenRequestException("Invalid Token");
        }

        String email = getEmailFromToken(HARDCODED_TOKEN); // Ensure this method extracts email from the token

        // Check if the user exists in the database
        //think if you need to add this check bcz the token cannot be generated if the user credentials are bad.
        Optional<User> userOptional = userRepository.findByEmail(email);
        log.info(userOptional.get().getEmail());
        log.info(userOptional.get().getRole().toString());
        if (!userOptional.isPresent()) {
            throw new ForbiddenRequestException("User not found with email: " + email);
        }
        else if("ADMIN".equals(userOptional.get().getRole().toString())) {
            return true;
        } else if (userOptional.get().getRole().toString().equals("USER") && requestType.equals("GET")) {
            return true;
        }
        else{
            throw new ForbiddenRequestException("Role Does not Have the required Access");
        }

    }




}














































//public class ValidationService {
//    private static final String secretKEY ="kI8jWFcSiLnsAjyuyYMg1U7fUwYr9JrZ1n/aVlKbh6I=";
//    private final JWTVerifier verifier;
//
//    public ValidationService() {
//        Algorithm algorithm = Algorithm.HMAC256(Decoders.BASE64.decode(secretKEY));
//        this.verifier = JWT.require(algorithm).build();
//    }
//
//    public boolean doesEmailMatch(String token, String userEmail) {
//        try {
//            DecodedJWT decodedJWT = verifier.verify(token);
//            String tokenEmail = decodedJWT.getSubject();
//            return tokenEmail.equals(userEmail);
//        } catch (JWTVerificationException e) {
//            throw new JWTVerificationException("JWT token verification failed: " + e.getMessage());
//        }
//    }
//
//    public String getEmailFromToken(String token) {
//        return decodeToken(token).getSubject();
//    }
//
//    private DecodedJWT decodeToken(String token) {
//        try {
//            return verifier.verify(token);
//        } catch (JWTVerificationException e) {
//            throw new JWTVerificationException("JWT token verification failed: " + e.getMessage());
//        }
//    }
//}






//public class ValidationService {
//    private final JwtService jwtService;
//
////    private final UserRepository userRepository;
////    public class JwtDecoder {
////
////        private static final String secretKEY = "c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl";
////
////        private Key getSigninKey() {
////            byte[] keyBytes = Decoders.BASE64.decode(secretKEY);
////            return Keys.hmacShaKeyFor(keyBytes);
////        }
////
////        public Claims decodeJWT(String jwt) {
////            return Jwts.parserBuilder()
////                    .setSigningKey(getSigninKey())
////                    .build()
////                    .parseClaimsJws(jwt)
////                    .getBody();
////        }
////
////    }
////    public boolean validateToken(String token){
////        JwtDecoder jwtDecoder = new JwtDecoder();
////        Claims claims = jwtDecoder.decodeJWT(token);
////        String subject = claims.getSubject();
////        Optional<User> findUser = userRepository.findByEmail(subject);
////        if (findUser.isPresent()){
////            return true;
////        }
////        else {
////            throw new InvalidJwtToken("ERROR RESOURCE NOT FOUND");
////        }
////
////    }
//////
//////
//////
////////    public boolean validateToken(String token) {
////////        JwtDecoder decoder = new JwtDecoder();
////////        Claims claims = decoder.decodeJWT(token);
////////        String subject = claims.getSubject();
////////        return user.getEmail().equals(subject);
////////
////////        final String email = jwtService.getEmailFromToken(token);
////////        return (email.equals(user.getEmail()) && !jwtService.isTokenExpired(token));
////////        DecodedJWT decodedJWT = JWT.decode(token);
////////        Date expiresAt = decodedJWT.getExpiresAt();
////////        Claims claims = decoder.decodeJWT(token);
////////    }
////
////        public boolean validateToken(String token) {
////
////
////        DecodedJWT decodedJWT;
////        try {
////            decodedJWT = JWT.decode(token);
////
////        } catch (JWTDecodeException e) {
////            throw new JWTDecodeException("Unable to Decode the required Jwt token");
////        }
////        Date expiresAt = decodedJWT.getExpiresAt();
////
////        return expiresAt.before(new Date());
////
////    }
//
//
//}
