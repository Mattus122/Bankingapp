package com.example.bankingapp.dto;

import com.example.bankingapp.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
