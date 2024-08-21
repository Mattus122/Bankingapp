package com.example.bankingapp.validation;
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
import java.util.Date;
import java.util.Optional;


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
        byte[] keyBytes = Decoders.BASE64.decode("c2VjcmV0S2V5RXhhbXBsZTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl");
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String getEmailFromToken(String token) {
        return extractAllClaims(token).get("email", String.class);
    }
    public String getPasswordFromToken(String token){return extractAllClaims(token).get("password", String.class);}
    public String getSubjectFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }
    public String returnRole(String token){
        return getSubjectFromToken(token);
    }



    public boolean isTokenExpired(String token) {
                Claims claims = extractAllClaims(token);
                Date expiration = claims.getExpiration();
                return expiration.before(new Date());

    }
    public boolean validateToken(String token , String requestType) {
        final String HARDCODED_TOKEN =token;

        // Token comparison
        if (!token.equals(HARDCODED_TOKEN)) {
            throw new ForbiddenRequestException("Invalid Token");
        }

        String email = getEmailFromToken(HARDCODED_TOKEN);

        Optional<User> userOptional = userRepository.findByEmail(email);
        log.info(userOptional.get().getEmail());
        log.info(userOptional.get().getRole().toString());
        if("ADMIN".equals(userOptional.get().getRole().toString())) {
            return true;
        } else if (userOptional.get().getRole().toString().equals("USER") && requestType.equals("GET")) {
            return true;
        }
        else{
            throw new ForbiddenRequestException("Role Does not Have the required Access");
        }
    }

}

