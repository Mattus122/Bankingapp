package com.example.bankingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObjectForValidations {
    private String message;
    private List<String> errordetails;

//    public ErrorObjectForValidations(String message, List<String> details) {
//        this.message = message;
//        this.details = details;
//    }

}
