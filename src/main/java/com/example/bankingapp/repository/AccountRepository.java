package com.example.bankingapp.repository;
import com.example.bankingapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account,UUID>{
    List<Account> findByUserId(UUID userId);
    void deleteById(UUID accountID);
}
