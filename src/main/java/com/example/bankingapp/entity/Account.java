package com.example.bankingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    @Column(name = "account_name")
    private String name;

    @Column(name = "account_balance")
    private BigDecimal balance;
    @Column(name = "account_currency")
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @ManyToOne//fetch type is eager by default
    @JoinColumn(name = "user_id")
    @JsonIgnore
//    @OneToOne
//    @JoinColumn(name = "user_id") with the help of this we can name our foreign key and gives info regarding column we are joining.
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private List<Transaction> transaction;
}