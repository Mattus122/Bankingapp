package com.example.bankingapp.dto;

import com.example.bankingapp.entity.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountDTO{
    @NotBlank(message = "Account name cannot be empty or null")
    private String name;
    private String currency;
    private AccountStatus accountStatus;
}
