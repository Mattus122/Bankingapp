package com.example.bankingapp.service;

import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.Transaction;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.jwtExcetion.ForbiddenRequestException;
import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.TransactionRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.validation.ValidationService;
import lombok.RequiredArgsConstructor;

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
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ValidationService validationService;
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
    public TransactionDTO createTransaction(UUID accountId, TransactionDTO transactionDTO , String token) throws AccountNotFoundException {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (validationService.returnRole(token).equals("ADMIN") ||validationService.returnRole(token).equals("USER")){
            Optional<Account> findbyid = accountRepository.findById(accountId);
            Account linkedaccount = null;
            if (findbyid.isPresent()) {
                linkedaccount = findbyid.get();
            }
            if (findbyid.isPresent()) {
                Transaction tr = Transaction.builder()
                        .transactionStatus(transactionDTO.getTransactionStatus()).transactionType(transactionDTO.getTransactionType())
                        .account(linkedaccount)
                        .build();
                Transaction transact = transactionRepository.save(tr);
                return convertToTransactionDTO(transact);

            } else {
                throw new UserNotFoundExcetion("Accont not found at AccountId :  "+accountId);
            }
        }
        return null;



    }

    public List<TransactionDTO> getAllTransactions(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.returnRole(token).equals("ADMIN")){
            List<Transaction> transactions = transactionRepository.findAll();

            List<TransactionDTO> transactionDTOList = transactions.stream()
                    .map(this::convertToTransactionDTO)
                    .collect(Collectors.toList());
            return transactionDTOList;
        }
        else{
            throw new ForbiddenRequestException("Role Does Not Have Required Access Permisiion");
        }

    }
    public List<TransactionDTO> getTransactionsByUserId(UUID userId , String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        List<Transaction> transactionList = new ArrayList<>();
        if(validationService.returnRole(token).equals("USER")){
            Optional<User> userOptional = userRepository.findByEmail(validationService.getEmailFromToken(token));
            UUID id = userOptional.get().getId();
            if(id.equals(userId)){
                transactionList = transactionRepository.findAllByUserId(userId);
                return transactionList.stream()
                        .map(this::convertToTransactionDTO)
                        .collect(Collectors.toList());
            }
            else{
                throw new ForbiddenRequestException("Cannot Access Other Users Data ");
            }
        }
        if(validationService.returnRole(token).equals("ADMIN")){
            List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
            return transactions.stream()
                    .map(this::convertToTransactionDTO)
                    .collect(Collectors.toList());
        }
        return null;

    }


    private TransactionDTO convertToTransactionDTO(Transaction transaction) {

        return TransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .transactionStatus(transaction.getTransactionStatus())
                .build();
    }



}

