package com.example.bankingapp.exception;

public class NoAccountFoundException extends RuntimeException{
    public NoAccountFoundException() {
    }
    public NoAccountFoundException(String message) {
        super(message);
    }
    public NoAccountFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
