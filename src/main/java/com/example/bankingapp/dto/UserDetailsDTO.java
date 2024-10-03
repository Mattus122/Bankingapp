package com.example.bankingapp.dto;

import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDTO{
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private OrganisationName organisationName;
    private int age;
    private List<Account> accountList;
}
