package com.example.bankingapp.exception;

import lombok.Data;


public class InvalidJwtToken extends RuntimeException {
    public InvalidJwtToken() {
    }
    public InvalidJwtToken(String message) {
        super(message);
    }
    public InvalidJwtToken(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidJwtToken(Throwable cause) {
        super(cause);
    }
}
