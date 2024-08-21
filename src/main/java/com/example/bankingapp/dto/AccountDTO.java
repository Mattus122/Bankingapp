package com.example.bankingapp.dto;

import com.example.bankingapp.entity.AccountStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private UUID accountId;


    @NotBlank(message = "Account name cannot be empty or null")
    private String name;

    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance cannot be negative")
    private int balance;

    private String currency;
    private AccountStatus accountStatus;


}
