package com.example.bankingapp.service;

import com.example.bankingapp.dto.ResponseTransactionDTO;
import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.entity.*;
import com.example.bankingapp.exception.NoAccountFoundException;
import com.example.bankingapp.exception.UserNotFoundException;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.TransactionRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.validation.TransactionValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final TransactionValidationService transactionValidationService;
    public ResponseTransactionDTO transactionCreation(String token,UUID accountId,
                                                      TransactionDTO transactionDTO)throws Exception{
        Account debtorAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoAccountFoundException("Account not found with id: "+accountId));
        Account creditorAccount = accountRepository.findById(transactionDTO.getCreditor())
                .orElseThrow(() -> new NoAccountFoundException("Account not found with id: "+accountId));
        transactionValidationService.validateTransactionCreation(token,debtorAccount,creditorAccount);
        TransactionStatus transactionStatusOfAccount = determineTransactionStatus
                (debtorAccount,transactionDTO.getTransactionAmount());
        Account linkedaccount = debtorAccount;
        updateAccountBalance(debtorAccount,creditorAccount,transactionDTO,transactionStatusOfAccount);
        Transaction tr = Transaction.builder().creditor(transactionDTO.getCreditor())
                .transactionAmount(transactionDTO.getTransactionAmount())
                .transactionStatus(transactionStatusOfAccount)
                .transactionType(transactionDTO.getTransactionType())
                .account(linkedaccount)
                .build();
        tr.setTransactionMessage(accountId,transactionDTO,transactionStatusOfAccount);
        Transaction transact = transactionRepository.save(tr);
        return convertToTransactionDTO(transact);
    }
    private boolean checkSameUser(String token, UUID accountId) {
        String emailFromToken = jwtService.getEmailFromToken(token);
        Optional<User> userOptional = userRepository.findByEmail(emailFromToken);
        List<Account> accountList= userOptional.get().getAccounts();
        for(Account a : accountList){
            if (a.getAccountId().equals(accountId)){
                return true;
            }
        }
        return false;
    }
    public List<ResponseTransactionDTO> getTransactionsByUserId(UUID userId,String token){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: "+userId));
        transactionValidationService.validateFindTransactionByUserId(user,token);
        List<Transaction> transactionList;
        transactionList = transactionRepository.findAllByUserId(userId);
        return transactionList.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }
    private ResponseTransactionDTO convertToTransactionDTO(Transaction transaction) {
        return ResponseTransactionDTO.builder()
                .creditor(transaction.getCreditor())
                .creationTimeStamp(transaction.getCreationTimeStamp())
                .transactionAmount(transaction.getTransactionAmount())
                .transactionMessage(transaction.getTransactionMessage())
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .transactionStatus(transaction.getTransactionStatus())
                .build();
    }
    public List<ResponseTransactionDTO> getAccountTransaction(UUID accountId, String token) {
        Account debtorAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoAccountFoundException("Account not found with id: "+accountId));
        transactionValidationService.validateFindTransactionByAccountId(token,debtorAccount);
        List<Transaction> transactionList = transactionRepository.findByAccountId(accountId);
        return transactionList.stream()
                .map(this::convertToTransactionDTO)
                .collect(Collectors.toList());
    }
    public TransactionStatus determineTransactionStatus(Account debtorAccount, int transactionAmount) {
        if (debtorAccount.getBalance()>transactionAmount) {
            return TransactionStatus.COMPLETED;
        } else {
            return TransactionStatus.FAILED;
        }
    }
    public void updateAccountBalance(Account debtorAccount
                                    ,Account creditorAccount,TransactionDTO transactionDTO
                                    ,TransactionStatus transactionStatusOfAccount){
        if(transactionStatusOfAccount.equals(TransactionStatus.COMPLETED)){
            int debtorBalance = debtorAccount.getBalance()-transactionDTO.getTransactionAmount();
            debtorAccount.setBalance(debtorBalance);
            int creditorBalance = creditorAccount.getBalance()+transactionDTO.getTransactionAmount();
            creditorAccount.setBalance(creditorBalance);
        }
    }
}

