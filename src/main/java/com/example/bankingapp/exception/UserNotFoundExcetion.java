package com.example.bankingapp.exception;

public class UserNotFoundExcetion extends RuntimeException{
    public UserNotFoundExcetion() {
    }

    public UserNotFoundExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundExcetion(Throwable cause) {
        super(cause);
    }

    public UserNotFoundExcetion(String message) {
        super(message);
    }


}
