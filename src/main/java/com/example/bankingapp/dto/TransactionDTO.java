package com.example.bankingapp.dto;

import com.example.bankingapp.entity.Transaction;
import com.example.bankingapp.entity.TransactionStatus;
import com.example.bankingapp.entity.TransactionType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private UUID transactionId;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
}
