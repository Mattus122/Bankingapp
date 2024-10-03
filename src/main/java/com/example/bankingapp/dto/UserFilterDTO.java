package com.example.bankingapp.dto;

import com.example.bankingapp.entity.OrganisationName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFilterDTO {
    private OrganisationName organisationName;
    private Date startDate;
    private Date endDate;
    private String firstName;
}
