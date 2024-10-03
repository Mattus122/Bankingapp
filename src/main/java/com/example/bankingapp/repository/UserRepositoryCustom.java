package com.example.bankingapp.repository;

import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.User;

import java.util.Date;
import java.util.List;

public interface UserRepositoryCustom {
    List<User> findUsersByParams(OrganisationName organisationName, Date startDate, Date endDate, String firstName);
}


