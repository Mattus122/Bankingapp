package com.example.bankingapp.service;

import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.Transaction;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
//    public ResponseEntity<Transaction> createTransaction( UUID accountId , Transaction transaction) {
//        Optional<Account> findById = accountsRepository.findById(accountId);
//        Account account = null;
//        if(findById.isPresent()){
//            account = findById.get();
//            Transaction tan  = Transaction.builder().transactionType(transaction.getTransactionType()).account(account).
//                    transactionStatus(transaction.getTransactionStatus()) .build();
//            Transaction tra = transactionRepository.save(tan);
//            return new ResponseEntity<>(tra, HttpStatus.CREATED);
//        }
//        else{
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//    }
    public TransactionDTO createTransaction(UUID accountId, Transaction transaction) throws AccountNotFoundException

    {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

        Transaction transact = Transaction.builder()
            .transactionType(transaction.getTransactionType())
            .transactionStatus(transaction.getTransactionStatus())
            .account(account)
            .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);

    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        List<TransactionDTO> transactionDTOList = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return transactionDTOList;
    }
    public List<TransactionDTO> getTransactionsByUserId(UUID userId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByUserId(userId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .transactionType(transaction.getTransactionType())
                .transactionStatus(transaction.getTransactionStatus())
                .build();
    }

}

