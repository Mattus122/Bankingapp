package com.example.bankingapp.controlleradvice;

import com.example.bankingapp.dto.ErrorObject;
import com.example.bankingapp.dto.ErrorObjectForValidations;
import com.example.bankingapp.exception.AccountCreationException;
import com.example.bankingapp.exception.NoAccountFoundException;
import com.example.bankingapp.exception.ForbiddenRequestException;
import com.example.bankingapp.exception.UnauthorizedAccessException;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import com.example.bankingapp.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class errorHandler{
//    @ExceptionHandler
//    public ResponseEntity<ErrorObject> handleException(Exception e){
//        ErrorObject er = ErrorObject.builder()
//                .message(e.getMessage())
//                .status(HttpStatus.BAD_REQUEST.value())
//                .timestamp(System.currentTimeMillis())
//                .build();
//        return new ResponseEntity<>(er , HttpStatus.BAD_REQUEST);
//    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Exception2(UserNotFoundException ex){
        ErrorObject er = ErrorObject.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(er , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Exception3(UserAlreadyExistsException e){
        ErrorObject er = ErrorObject.builder()
                .timestamp(System.currentTimeMillis())
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();
        return new ResponseEntity<>(er , HttpStatus.CONFLICT);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> Exception4(NoAccountFoundException e){
        ErrorObject er = ErrorObject.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(er , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorObject> Exception5(ForbiddenRequestException e){
        ErrorObject er = ErrorObject.builder()
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.FORBIDDEN.value())
                .build();
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

        ErrorObjectForValidations errorObjectForValidations
                = new ErrorObjectForValidations("Validation Failed", errors);
        return ResponseEntity.badRequest().body(errorObjectForValidations);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorObject> handle(AccountCreationException e){
        ErrorObject er = ErrorObject.builder()
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(er , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler()
    public ResponseEntity<ErrorObject> handle(UnauthorizedAccessException e){
        ErrorObject er = ErrorObject.builder()
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<>(er , HttpStatus.UNAUTHORIZED);
    }

}
