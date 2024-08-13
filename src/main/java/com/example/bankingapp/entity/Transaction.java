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
@NamedQuery(
        name = "Transaction.findAllByUserId",
        query = "SELECT t FROM Transaction t " +
                "JOIN t.account a " +
                "JOIN a.user u " +
                "WHERE u.id = :userId"
)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_id" , nullable = false)
    private Account account;

}

