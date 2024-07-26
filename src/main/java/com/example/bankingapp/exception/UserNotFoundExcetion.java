package com.example.bankingapp.exception;

public class UserNotFoundExcetion extends RuntimeException{
    public UserNotFoundExcetion() {
    }

    public UserNotFoundExcetion(String message) {
        super(message);
    }


}
