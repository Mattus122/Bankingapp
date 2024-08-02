package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.AccountNotFound;
import com.example.bankingapp.exception.UserNotFoundExcetion;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final ValidationService validationService;
    public AccountDTO createaccount (UUID userId, Account account) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExcetion("User not found for id : "+userId));
//
//        Account newAccount = new Account();
//        account.setName(account.getName());
//        account.setBalance(account.getBalance());
//        account.setCurrency(account.getCurrency());
//        account.setAccountStatus(account.getAccountStatus());
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);
        return convertEntityTOAccountDto(savedAccount);
//        Optional<User> findbyid = userRepository.findById(userId);
//        User user = null;
//        if (findbyid.isPresent()) {
//            user = findbyid.get();
//        }
//        if (findbyid.isPresent()) {
//            Account acc = Account.builder().name(account.getName())
//                    .balance(account.getBalance()).
//                    currency(account.getCurrency()).
//                    accountStatus(account.getAccountStatus())
//                    .user(user)
//                    .build();
//            Account newaccount = accountsRepository.save(acc);
//            AccountDto accountDto = convertEntityTOAccountDto(newaccount);
//            return new ResponseEntity<>( accountDto , HttpStatus.CREATED);
//        } else {
//            throw new UserNotFoundExcetion("User Not Found at given id : "+userId);
//        }
    }


//    public List<AccountDto> accountInformation(UUID userId) throws Exception {
//        List<Account> accountsList = new ArrayList<>();
//////        userRepository.findAll().forEach(user -> {
//////            if (user.getId().equals(userId)) {
//////                accountsList.add(userRepository.fi);
//////            }
//////        });
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isPresent()) {
//            List<Account> accounts = accountRepository.findByUserId(userId);
//            return accounts.stream()
//                    .map(this::convertEntityTOAccountDto)
//                    .collect(Collectors.toList());
//        } else throw new AccountNotFoundException("NO Account present with user id" + userId);
//
//        //working code incase off returning List<Accounts>>
////        Optional<User> userOptional = userRepository.findById(userId);
////        if (userOptional.isPresent()) {
////            List<Account> accounts =userOptional.get().getAccounts();
////            return accounts.stream()
////                    .map(account -> {
////                        AccountDto accountDto = new AccountDto();
////                        accountDto.setName(account.getName());
////                        accountDto.setAccountStatus(account.getAccountStatus());
////                        accountDto.setBalance(account.getBalance());
////                        accountDto.setCurrency(account.getCurrency());
////                        return accountDto;
////                    })
////                    .collect(Collectors.toList());
////        } else {
////            return null;
////        }
//    }
    public List<AccountDTO> getAccountInformation(UUID userId) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundExcetion("No user present with user ID " + userId));
        List<Account> accounts = accountRepository.findByUserId(userId);
        if(accounts.isEmpty()){
            throw new AccountNotFoundException("No account exists for id" +userId);
        }
        return accounts.stream()
            .map(this::convertEntityTOAccountDto)
            .collect(Collectors.toList());
    }
    public AccountDTO convertEntityTOAccountDto(Account account){
        AccountDTO accountDto = new AccountDTO();
        accountDto.setAccountStatus(account.getAccountStatus());
        accountDto.setCurrency(account.getCurrency());
        accountDto.setName(account.getName());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }

    public AccountDTO updateAccount(UUID accountId, AccountDTO accountDTO) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (!accountOptional.isPresent()) {
            throw new AccountNotFound("Account not found with id: " + accountId);
        }

        Account account = accountOptional.get();
        account.setName(accountDTO.getName());
        account.setBalance(accountDTO.getBalance());
        account.setCurrency(accountDTO.getCurrency());
        account.setAccountStatus(accountDTO.getAccountStatus());

        Account updatedAccount = accountRepository.save(account);

        return convertEntityTOAccountDto(updatedAccount);
    }

    public void deleteAccount(UUID accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AccountNotFound("Account not found with id: " + accountId);
        }

        accountRepository.deleteById(accountId);
    }
}

