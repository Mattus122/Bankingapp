package com.example.bankingapp.repository;

import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t FROM Transaction t JOIN t.account a JOIN a.user u WHERE u.id = :userId")
    List<Transaction> findAllByUserId(@Param("userId") UUID userId);


}
