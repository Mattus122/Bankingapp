package com.example.bankingapp.exception;

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
