package com.example.bankingapp.entity;

import com.example.bankingapp.dto.TransactionDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedQuery(
        name = "Transaction.findByAccountId",
        query = "SELECT t FROM Transaction t WHERE t.account.id = :accountId"
)
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
    @Transient
    private String transactionMessage;
    @Column
    private UUID creditor;
    private int transactionAmount;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType  transactionType;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;
    @CreationTimestamp
    @Column(nullable = false , updatable = false)
    private Date creationTimeStamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_id" , nullable = false)
    private Account account;
    public void setTransactionMessage(UUID accountId, TransactionDTO transactionDTO,TransactionStatus transactionStatus) {
        if(transactionStatus.equals(TransactionStatus.COMPLETED)){
            this.transactionMessage = "Amount "+transactionDTO.getTransactionAmount()
                            + " is debited from "+accountId
                            + " and credited to " +transactionDTO.getCreditor();
        }else if(transactionStatus.equals(transactionStatus.FAILED)){
            this.transactionMessage = "Amount " + transactionDTO.getTransactionAmount()
                    + " is not " + transactionDTO.getTransactionType()
                    + " from " + accountId;
        }
    }

}

