package com.example.bankingapp.controller;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.service.AccountService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Account" , description = "Account Management Api ")
public class AccountController {
    Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    @GetMapping("/user/{userId}/account")
    ResponseEntity<List<AccountDTO>> getaccountInfo(@PathVariable UUID userId , @RequestHeader("Authorization") String token ) throws Exception {
        List<AccountDTO>accountDTOList =  accountService.getAccountInformation(userId , token);
        return new ResponseEntity<>(accountDTOList , HttpStatus.OK);

    }
    @PostMapping("user/{userId}/account")
    ResponseEntity<AccountDTO> create(@PathVariable UUID userId ,  @Valid @RequestBody  AccountDTO accountDTO , @RequestHeader("Authorization") String token) throws Exception {
        AccountDTO createdAccount = accountService.createaccount(userId , accountDTO , token);
        return new ResponseEntity<>(createdAccount , HttpStatus.CREATED);
    }
    @PutMapping("user/account/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable UUID accountId, @Valid @RequestBody  AccountDTO accountDTO , @RequestHeader("Authorization") String token) {
        AccountDTO acc = accountService.updateAccount(accountId ,accountDTO , token);
        return new ResponseEntity<>(acc , HttpStatus.OK);
    }
    @DeleteMapping("/user/account/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountId , @RequestHeader("Authorization") String token) {
        accountService.deleteAccount(accountId , token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

