package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.UserNotFoundExcetion;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public ResponseEntity<AccountDTO> createaccount(UUID userId, AccountDTO accountDto) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundExcetion("User not found for id : "+userId));
        Account account = new Account();
        account.setName(accountDto.getName());
        account.setBalance(accountDto.getBalance());
        account.setCurrency(accountDto.getCurrency());
        account.setAccountStatus(accountDto.getAccountStatus());
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);
        return new ResponseEntity<>(accountDto , HttpStatus.CREATED);
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
}
