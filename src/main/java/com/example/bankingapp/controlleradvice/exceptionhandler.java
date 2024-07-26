package com.example.bankingapp.controlleradvice;

import com.example.bankingapp.dto.ErrorObject;
import com.example.bankingapp.exception.AccountNotFound;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import com.example.bankingapp.exception.UserNotFoundExcetion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class exceptionhandler {
    @ExceptionHandler
    public ResponseEntity<ErrorObject> handleExcetion(Exception e){
        ErrorObject er = ErrorObject.builder().message(e.getMessage()).status(HttpStatus.BAD_REQUEST.value()).timestamp(System.currentTimeMillis()).build();
        return new ResponseEntity<>(er , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Excetion2(UserNotFoundExcetion ex){
        ErrorObject er = ErrorObject.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND.value()).timestamp(System.currentTimeMillis()).build();
        return new ResponseEntity<>(er , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Exception3(UserAlreadyExistsException e){
        ErrorObject er = ErrorObject.builder().timestamp(System.currentTimeMillis()).message(e.getMessage()).status(HttpStatus.CONFLICT.value()).build();
        return new ResponseEntity<>(er , HttpStatus.CONFLICT);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Exception3(AccountNotFound e){
        ErrorObject er = ErrorObject.builder().message(e.getMessage()).status(HttpStatus.NO_CONTENT.value()).timestamp(System.currentTimeMillis()).build();
        return new ResponseEntity<>(er , HttpStatus.NO_CONTENT);
    }
}
