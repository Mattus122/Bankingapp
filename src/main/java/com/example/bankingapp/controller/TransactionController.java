package com.example.bankingapp.controller;

import com.example.bankingapp.dto.ResponseTransactionDTO;
import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
            @ApiResponse(responseCode = "201", description = "Transaction created successfully", content = { @Content(schema = @Schema(implementation = ResponseTransactionDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Account not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("user/account/{accountId}/transactions")
    public ResponseEntity<ResponseTransactionDTO> createTransaction(@PathVariable UUID accountId, @Valid @RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token ) throws AccountNotFoundException {
        ResponseTransactionDTO createdResponseTransactionDTO = transactionService.createTransaction(accountId, transactionDTO , token);
        return new ResponseEntity<>(createdResponseTransactionDTO, HttpStatus.CREATED);
    }
    @Operation(
            summary = "Get All Transactions",
            description = "Retrieve a list of all transactions. The response is a list of TransactionDTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully", content = { @Content(schema = @Schema(implementation = ResponseTransactionDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "No transactions found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
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
