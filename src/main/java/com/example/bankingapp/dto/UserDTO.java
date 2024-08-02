package com.example.bankingapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String email;

    private String firstName;


    private String lastName;


    private LocalDate dob;

    private int age;

}
