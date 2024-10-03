package com.example.bankingapp.dto;

import com.example.bankingapp.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    @NotNull(message = "Transaction amount  cannot be null")
    @PositiveOrZero(message = "Balance cannot be negative")
    private int transactionAmount;
    @NotNull(message = "Transaction Type  cannot be null")
    private TransactionType transactionType;
    private UUID creditor;
    @NotNull
    @Size(min = 3, max = 3, message = "Currency must be a 3-character code")
    private String currency;
}
