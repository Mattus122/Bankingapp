package com.example.bankingapp;

import com.example.bankingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestH2Repository  extends JpaRepository<User , UUID> {
}
