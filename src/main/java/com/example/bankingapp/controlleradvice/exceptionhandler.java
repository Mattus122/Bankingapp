package com.example.bankingapp.controlleradvice;

import com.example.bankingapp.dto.ErrorObject;
import com.example.bankingapp.dto.ErrorObjectForValidations;
import com.example.bankingapp.exception.accountexception.AccountNotFound;
import com.example.bankingapp.exception.jwtExcetion.ForbiddenRequestException;
import com.example.bankingapp.exception.jwtExcetion.InvalidJwtToken;
import com.example.bankingapp.exception.userexception.UserAlreadyExistsException;
import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<ErrorObject> Exception4(AccountNotFound e){
        ErrorObject er = ErrorObject.builder().message(e.getMessage()).status(HttpStatus.NO_CONTENT.value()).timestamp(System.currentTimeMillis()).build();
        return new ResponseEntity<>(er , HttpStatus.NO_CONTENT);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Excetion(InvalidJwtToken e){
        ErrorObject er =  ErrorObject.builder().timestamp(System.currentTimeMillis()).status(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build();
        return new ResponseEntity<>(er , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Exception5(ForbiddenRequestException e){
        ErrorObject er = ErrorObject.builder()
                .message(e.getMessage()).timestamp(System.currentTimeMillis()).status(HttpStatus.FORBIDDEN.value()).build();
        return new ResponseEntity<>(er , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObjectForValidations> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            if (error instanceof FieldError) {
                errorMessage = ((FieldError) error).getField() + ": " + errorMessage;
            }
            errors.add(errorMessage);
        });

        ErrorObjectForValidations errorObjectForValidations = new ErrorObjectForValidations("Validation Failed", errors);
        return ResponseEntity.badRequest().body(errorObjectForValidations);
    }
}
