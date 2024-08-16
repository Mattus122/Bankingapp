package com.example.bankingapp.service;

import com.example.bankingapp.dto.ResponseTransactionDTO;
import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.entity.*;
import com.example.bankingapp.exception.accountexception.NoAccountFoundException;
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
    public ResponseTransactionDTO createTransaction(UUID accountId, TransactionDTO transactionDTO, String token) throws AccountNotFoundException {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.returnRole(token).equals("ADMIN")){
            throw new ForbiddenRequestException("Cannot Create a Transaction ADMIN  does not have access for account creation of its own");
        }

        Optional<Account> findbyid = accountRepository.findById(accountId);
        Account linkedaccount = null;
        if (findbyid.isPresent() && transactionDTO.getTransactionType().toString().equals("DEBIT") && checkifSameUser(token , accountId )) {
            int balance = transactionDTO.getTransactionAmount() + findbyid.get().getBalance();
            findbyid.get().setBalance(balance);
            linkedaccount = findbyid.get();
            Transaction tr = Transaction.builder().transactionMessage("Amount " + transactionDTO.getTransactionAmount()+ " is "+transactionDTO.getTransactionType() + " into " +accountId)
                    .transactionAmount(transactionDTO.getTransactionAmount()).transactionStatus(TransactionStatus.COMPLETED).transactionType(transactionDTO.getTransactionType())
                    .account(linkedaccount)
                    .build();
            Transaction transact = transactionRepository.save(tr);
            return convertToTransactionDTO(transact);
        }
        else if(transactionDTO.getTransactionType().toString().equals("CREDIT")&& checkifSameUser(token , accountId)){
            linkedaccount = findbyid.get();
            if(findbyid.get().getBalance() > transactionDTO.getTransactionAmount()){
                int balance = findbyid.get().getBalance() - transactionDTO.getTransactionAmount();
                findbyid.get().setBalance(balance);
                Transaction tr = Transaction.builder().transactionMessage("Amount " + transactionDTO.getTransactionAmount()+ " is "+transactionDTO.getTransactionType() + " from " +accountId)
                        .transactionAmount(transactionDTO.getTransactionAmount()).transactionStatus(TransactionStatus.COMPLETED).transactionType(transactionDTO.getTransactionType())
                        .account(linkedaccount)
                        .build();
                Transaction transact = transactionRepository.save(tr);
                return convertToTransactionDTO(transact);
            }
            else{
                linkedaccount = findbyid.get();
                Transaction tr = Transaction.builder().transactionMessage( "Low account Balance ")
                        .transactionAmount(transactionDTO.getTransactionAmount()).transactionStatus(TransactionStatus.FAILED).transactionType(transactionDTO.getTransactionType())
                        .account(linkedaccount)
                        .build();
                transactionRepository.save(tr);
                return convertToTransactionDTO(tr);
            }

        }
        else {
            throw new NoAccountFoundException("No account Present for  id : "+accountId);
        }
    }

    private boolean checkifSameUser(String token, UUID accountId) {
        String emailFromToken = validationService.getEmailFromToken(token);
        Optional<User> userOptional = userRepository.findByEmail(emailFromToken);
        List<Account> accountList= userOptional.get().getAccounts();
        for(Account a : accountList){
            if (a.getAccountId().equals(accountId)){
                return true;
            }
        }
        throw new ForbiddenRequestException("Cannot create a transaction, No Account with id "+ accountId + " exsists for user  " +emailFromToken);

    }


    public List<ResponseTransactionDTO> getAllTransactions(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.returnRole(token).equals("ADMIN")){
            List<Transaction> transactions = transactionRepository.findAll();

            List<ResponseTransactionDTO> responseTransactionDTOList = transactions.stream()
                    .map(this::convertToTransactionDTO)
                    .collect(Collectors.toList());
            return responseTransactionDTOList;
        }
        else{
            throw new ForbiddenRequestException("Role Does Not Have Required Access Permisiion");
        }

    }
    public List<ResponseTransactionDTO> getTransactionsByUserId(UUID userId , String token) {
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


    private ResponseTransactionDTO convertToTransactionDTO(Transaction transaction) {

        return ResponseTransactionDTO.builder()
                .transactionAmount(transaction.getTransactionAmount())
                .transactionMessage(transaction.getTransactionMessage())
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .transactionStatus(transaction.getTransactionStatus())
                .build();
    }



}

