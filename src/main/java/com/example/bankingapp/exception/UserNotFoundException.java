package com.example.bankingapp.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
    }
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}
