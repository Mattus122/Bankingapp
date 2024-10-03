package com.example.bankingapp.dto;

import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotEmpty(message = "email cannot be null")
    @Email
    private String email;
    @NotEmpty
    @Size(min = 2, message = "password should have at least 2 characters")
    private String password;
    @NotEmpty
    @Size(min = 2, message = "user firstName should have at least 2 characters")
    private String firstName;
    @NotEmpty
    @Size(min = 2, message = "user lastName should have at least 2 characters")
    private String lastName;
    @NotNull
    private Role role;
    @Min(value = 1, message = "Invalid Age: Equals to zero or Less than zero")
    @Max(value = 100, message = "Invalid Age: Exceeds 100 years")
    private int age;
    @NotNull
    private OrganisationName organisationName;

}
