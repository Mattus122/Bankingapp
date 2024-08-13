package com.example.bankingapp.dto;

import com.example.bankingapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenDTO {
    private String email;
    private Role role;

}
