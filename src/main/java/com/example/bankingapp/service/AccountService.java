package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.dto.UpdateAccountDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.NoAccountFoundException;
import com.example.bankingapp.exception.UserNotFoundException;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.validation.AccountValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Tag(name = "Account Management Api")
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountValidationService accountValidationService;
    public Account createAccount(UUID userId,AccountDTO accountDTO,String token){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: "+userId));
        accountValidationService.validateAccountPostCreation(token,user);
        Account account=Account .builder()
                                .name(accountDTO.getName())
                                .balance(accountDTO.getBalance())
                                .currency(accountDTO.getCurrency())
                                .accountStatus(accountDTO.getAccountStatus())
                                .user(user)
                                .build();
        account.setBankHolderNameFromUser();
        return accountRepository.save(account);
    }
    public List<Account> getAccountInformation(UUID userId,String token){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        accountValidationService.validateAccountInfo(token,user);
        return new ArrayList<>(user.getAccounts());
    }
    public AccountDTO updateAccount(UUID accountId,UpdateAccountDTO updateAccountDTO,String token){
        Account updateAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoAccountFoundException("Account not found with id: "+accountId));
        accountValidationService.validateAccountUpdation(token,updateAccount);
        updateAccount.setName(updateAccountDTO.getName());
        updateAccount.setCurrency(updateAccountDTO.getCurrency());
        updateAccount.setAccountStatus(updateAccountDTO.getAccountStatus());
        updateAccount = accountRepository.save(updateAccount);
        return convertEntityTOAccountDto(updateAccount);
    }
    public void deleteAccount(UUID accountId,String token){
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoAccountFoundException("Account not found with id: "+accountId));
        accountValidationService.validateAccountDeletion(token,account);
        accountRepository.deleteById(accountId);
    }
    public AccountDTO convertEntityTOAccountDto(Account account){
        AccountDTO accountDto = new AccountDTO();
        accountDto.setAccountId(account.getAccountId());
        accountDto.setAccountStatus(account.getAccountStatus());
        accountDto.setCurrency(account.getCurrency());
        accountDto.setName(account.getName());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }
}