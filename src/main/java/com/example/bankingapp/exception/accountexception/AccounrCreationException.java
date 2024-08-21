package com.example.bankingapp.exception.accountexception;

public class AccounrCreationException extends RuntimeException{
    public AccounrCreationException() {
    }

    public AccounrCreationException(String message) {
        super(message);
    }

    public AccounrCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccounrCreationException(Throwable cause) {
        super(cause);
    }
}
