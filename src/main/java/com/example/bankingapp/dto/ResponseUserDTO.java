package com.example.bankingapp.dto;

import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDTO {
    private UUID id;
    private String email ;
    private String firstName;
    private String lastName;
    private Role role;
    private OrganisationName organisationName;
    private Date creationTimeStamp;
}
