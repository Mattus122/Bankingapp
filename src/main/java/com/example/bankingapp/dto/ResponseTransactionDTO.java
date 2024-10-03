package com.example.bankingapp.dto;

import com.example.bankingapp.entity.TransactionStatus;
import com.example.bankingapp.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTransactionDTO {
    private UUID transactionId;
    private UUID creditor;
    private Date creationTimeStamp;
    private int transactionAmount;
    private String transactionMessage;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
}
