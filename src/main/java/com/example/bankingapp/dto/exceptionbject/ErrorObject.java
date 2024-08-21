package com.example.bankingapp.dto.exceptionbject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObject {

    private int status;
    private String message;
    private Long timestamp;
}
