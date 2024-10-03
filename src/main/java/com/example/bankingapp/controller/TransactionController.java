package com.example.bankingapp.controller;

import com.example.bankingapp.aspect.RoleCheck;
import com.example.bankingapp.dto.ResponseTransactionDTO;
import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor

@Tag(name = "Transaction ",description = "Transaction Management Api ")
public class TransactionController {
    private  final TransactionService transactionService;
    @PostMapping("user/account/{accountId}/transaction")
    @RoleCheck(roles = {Role.USER})
    ResponseEntity<ResponseTransactionDTO> createTransaction(@PathVariable UUID accountId,
                                                             @Valid @RequestBody TransactionDTO transactionDTO,
                                                             @RequestHeader("Authorization") String token)throws Exception {
        return new ResponseEntity<>(transactionService.transactionCreation(token,accountId,transactionDTO),HttpStatus.CREATED);
    }
    @GetMapping("user/{userId}/account/transactions")
    @RoleCheck(roles = {Role.USER,Role.BANKADMIN})
    ResponseEntity<List<ResponseTransactionDTO>> getTransactionsByUserId(@PathVariable UUID userId,
                                                                         @RequestHeader("Authorization")String token){
        List<ResponseTransactionDTO> transactions = transactionService.getTransactionsByUserId(userId , token);
        if (transactions.isEmpty()) {return new ResponseEntity<>(HttpStatus.NO_CONTENT);}
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    @GetMapping("/user/account/{accountId}/transactions")
    ResponseEntity<List<ResponseTransactionDTO>> accountTransaction(@PathVariable UUID accountId,
                                                                    @RequestHeader("Authorization")String token){
        List<ResponseTransactionDTO> transactionDTOList = transactionService.getAccountTransaction(accountId , token);
        if(transactionDTOList.isEmpty()){return new ResponseEntity<>(transactionDTOList , HttpStatus.NO_CONTENT);}
        return new ResponseEntity<>(transactionDTOList , HttpStatus.OK);
    }

}
