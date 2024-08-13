package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.accountexception.AccountNotFound;
import com.example.bankingapp.exception.jwtExcetion.ForbiddenRequestException;
import com.example.bankingapp.exception.jwtExcetion.InvalidJwtToken;
import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final ValidationService validationService;

    public AccountDTO createaccount (UUID userId, AccountDTO accountDTO , String token) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        validationService.validateToken(token , "POST");
        Optional<User> findbyid = userRepository.findById(userId);
        User user = null;
        if (findbyid.isPresent()) {
            user = findbyid.get();
        }
        if (findbyid.isPresent()) {
            Account acc = Account.builder().name(accountDTO.getName())
                    .balance(accountDTO.getBalance()).
                    currency(accountDTO.getCurrency()).
                    accountStatus(accountDTO.getAccountStatus())
                    .user(user)
                    .build();
            Account newaccount = accountRepository.save(acc);
            return convertEntityTOAccountDto(newaccount);

        } else {
            throw new UserNotFoundExcetion("User Not Found at given id : "+userId);
        }
    }



    public List<AccountDTO> getAccountInformation(UUID userId , String token) throws Exception {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = validationService.getEmailFromToken(token);
        Optional<User> userOptional = userRepository.findByEmail(email);
         List<AccountDTO> accountDTOList = new ArrayList<>();
        if(validationService.returnRole(token).equals("USER")){
            if(!userOptional.get().getId().equals(userId)){
                throw new ForbiddenRequestException("Cannot Access Other Users Account Information");
            }
            accountDTOList= getAccountListDTO(userId , email);
            return accountDTOList;
        }
        else {
            accountDTOList = getAccountListDTO(userId , email);
            return accountDTOList;
        }



    }
    private List<AccountDTO> getAccountListDTO( UUID userId, String email) throws AccountNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            List<Account> accounts = accountRepository.findByUserId(userId);
            if(accounts.isEmpty()){
                throw new AccountNotFoundException("No account exists for id" +userId);
            }
            return accounts.stream()
                    .map(this::convertEntityTOAccountDto)
                    .collect(Collectors.toList());
        }
        return null;
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

    public AccountDTO updateAccount(UUID accountId, AccountDTO accountDTO , String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.validateToken(token , "PUT")){
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
        throw new InvalidJwtToken("Pata nhi kya hua ");
    }

    public void deleteAccount(UUID accountId , String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Validate the token and check permissions
        validationService.validateToken(token, "DELETE");

        // Find the account by ID
        Optional<Account> findAccount = accountRepository.findById(accountId);
        if (findAccount.isPresent()){
            log.info(findAccount.get().getAccountId().toString());
            accountRepository.delete(findAccount.get());
        }



        else {
            throw new AccountNotFound("Account not found for ID " + accountId);
        }

    }

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