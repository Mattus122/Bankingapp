package com.example.bankingapp.service;

import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.dto.JwtTokenResponse;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.timedinterface.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Timed
    public JwtTokenResponse createJwtToken(JwtTokenDTO jwtTokenDTO ) {
        String jwt = jwtService.generateToken(jwtTokenDTO);
        return JwtTokenResponse.builder().accessToken(jwt).build();
    }
}
