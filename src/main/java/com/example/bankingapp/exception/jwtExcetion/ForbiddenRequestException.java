package com.example.bankingapp.exception.jwtExcetion;

public class ForbiddenRequestException extends RuntimeException {
    public ForbiddenRequestException() {
    }

    public ForbiddenRequestException(String message) {
        super(message);
    }

    public ForbiddenRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenRequestException(Throwable cause) {
        super(cause);
    }
}
