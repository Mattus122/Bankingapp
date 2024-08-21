package com.example.bankingapp.controller;

import com.example.bankingapp.dto.ResponseTransactionDTO;
import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor

@Tag(name = "Transaction " , description = "Transaction Management Api ")
public class TransactionController {
    private  final TransactionService transactionService;

    @PostMapping("user/account/{accountId}/transactions")
    public ResponseEntity<ResponseTransactionDTO> createTransaction(@PathVariable UUID accountId, @Valid @RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token ) throws AccountNotFoundException {
        ResponseTransactionDTO createdResponseTransactionDTO = transactionService.createTransaction(accountId, transactionDTO , token);
        return new ResponseEntity<>(createdResponseTransactionDTO, HttpStatus.CREATED);
    }

    @GetMapping("/transactions")
    ResponseEntity<List<ResponseTransactionDTO>> getAllTransactions(@RequestHeader("Authorization") String token){
        List<ResponseTransactionDTO> allTransactions = transactionService.getAllTransactions(token);
        if(allTransactions.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allTransactions , HttpStatus.OK);
    }
    @GetMapping("user/{userId}/account/transactions")
    public ResponseEntity<List<ResponseTransactionDTO>> getTransactionsByUserId(@PathVariable UUID userId , @RequestHeader("Authorization") String token ) {
        List<ResponseTransactionDTO> transactions = transactionService.getTransactionsByUserId(userId , token);
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    @GetMapping("/user/account/{accountId}/transactions")
    public ResponseEntity<List<ResponseTransactionDTO>> accountTransaction(@PathVariable UUID accountId , @RequestHeader("Authorization") String token) {
        List<ResponseTransactionDTO> transactionDTOList = transactionService.getAccountTransaction(accountId , token);

        if(transactionDTOList.isEmpty()){
            return new ResponseEntity<>(transactionDTOList , HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(transactionDTOList , HttpStatus.OK);
    }

}
