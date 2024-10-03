package com.example.bankingapp.controller;
import com.example.bankingapp.aspect.RoleCheck;
import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.dto.UpdateAccountDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.Role;
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
@RequestMapping("/v1/user")
@Tag(name = "Account" , description = "Account Management Api ")
@RequiredArgsConstructor
public class AccountController {
    Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    @GetMapping("user/{userId}/account")
    ResponseEntity<List<Account>> getAccountInfo(@PathVariable UUID userId,
                                                 @RequestHeader("Authorization")String token) {
        return new ResponseEntity<>(accountService.getAccountInformation(userId,token),HttpStatus.OK);
    }
    @PostMapping("user/{userId}/account")
    @RoleCheck(roles = {Role.SUPERADMIN,Role.BANKADMIN})
    ResponseEntity<Account> create(@PathVariable UUID userId,
                                   @Valid @RequestBody  AccountDTO accountDTO,
                                   @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(accountService.createAccount(userId,accountDTO,token),HttpStatus.CREATED);
    }
    @PutMapping("user/account/{accountId}")
    @RoleCheck(roles = {Role.SUPERADMIN,Role.BANKADMIN})
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable UUID accountId
                                                    ,@Valid @RequestBody UpdateAccountDTO updateAccountDTO
                                                    ,@RequestHeader("Authorization") String token){
        return new ResponseEntity<>(accountService.updateAccount(accountId,updateAccountDTO,token),HttpStatus.OK);
    }
    @DeleteMapping("user/account/{accountId}")
    @RoleCheck(roles = {Role.SUPERADMIN,Role.BANKADMIN})
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountId,
                                              @RequestHeader("Authorization")String token){
        accountService.deleteAccount(accountId,token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

