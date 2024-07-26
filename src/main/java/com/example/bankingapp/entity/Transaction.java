package com.example.bankingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "Transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @Column(name = "transaction_id" )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType  transactionType;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private Account account;

}

