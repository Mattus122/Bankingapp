package com.example.bankingapp.exception.accountexception;

public class AccountNotFound extends RuntimeException{
    public AccountNotFound() {
    }

    public AccountNotFound(String message) {
        super(message);
    }

    public AccountNotFound(String message, Throwable cause) {
        super(message, cause);
    }


}
