package com.example.bankingapp.validation;

import org.springframework.stereotype.Component;

@Component
public class TokenValidation {
    public String removeBearer(String token){
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }

}
