package com.example.bankingapp.controller;

import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.entity.Transaction;
import com.example.bankingapp.entity.TransactionStatus;
import com.example.bankingapp.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Transaction" , description = "Transaction  Management Api ")
public class TransactionController {
    private  final TransactionService transactionService;
    @Operation(
            summary = "Create a Transaction for an Account",
            description = "Create a Transaction for a given Account ID. The response is a TransactionDTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction created successfully", content = { @Content(schema = @Schema(implementation = TransactionDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Account not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/transaction/{accountId}")
    public ResponseEntity<TransactionDTO> createTransaction(@PathVariable UUID accountId, @RequestBody Transaction transaction) throws AccountNotFoundException {
        TransactionDTO createdTransactionDTO = transactionService.createTransaction(accountId, transaction);
        return new ResponseEntity<>(createdTransactionDTO , HttpStatus.CREATED);
    }
    @Operation(
            summary = "Get All Transactions",
            description = "Retrieve a list of all transactions. The response is a list of TransactionDTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully", content = { @Content(schema = @Schema(implementation = TransactionDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "No transactions found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/transactions")
    ResponseEntity<List<TransactionDTO>> getAllTransactions(){
        List<TransactionDTO> allTransactions = transactionService.getAllTransactions();
        if(allTransactions.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allTransactions , HttpStatus.OK);
    }
}
