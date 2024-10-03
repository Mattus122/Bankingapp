package com.example.bankingapp.dto;

import com.example.bankingapp.entity.AccountStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@Builder
public class AccountDTO {
    private UUID accountId;
    @NotBlank(message = "Account name cannot be empty or null")
    private String name;
    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance cannot be negative")
    private int balance;
    private String currency;
    private AccountStatus accountStatus;

    public AccountDTO() {}

    public UUID getAccountId() {
        return accountId;
    }
    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountId=" + accountId +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", accountStatus=" + accountStatus +
                '}';
    }
}
