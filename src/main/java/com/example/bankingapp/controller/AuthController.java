package com.example.bankingapp.controller;

import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.dto.JwtTokenResponse;
import com.example.bankingapp.service.AuthService;
import com.example.bankingapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor

@Tag(name = "Authorization" , description = "Authorization Management Api ")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/authorization")
    public ResponseEntity<JwtTokenResponse> generateJwtToken(@RequestBody JwtTokenDTO jwtTokenDTO){
        JwtTokenResponse jwtTokenResponse = authService.createJwtToken(jwtTokenDTO );
        return new ResponseEntity<>(jwtTokenResponse, HttpStatus.OK);
    }
}
