package com.example.bankingapp.dto;

import com.example.bankingapp.entity.TransactionStatus;
import com.example.bankingapp.entity.TransactionType;
import com.example.bankingapp.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
}
