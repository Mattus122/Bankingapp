package com.example.bankingapp.dto;

import com.example.bankingapp.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private String name;
    private BigDecimal balance;
    private String currency;
    private AccountStatus accountStatus;
}
