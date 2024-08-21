//package com.example.bankingapp.servicetest;
//
//import com.example.bankingapp.dto.AccountDTO;
//import com.example.bankingapp.dto.ResponseUserDTO;
//import com.example.bankingapp.dto.UserDTO;
//import com.example.bankingapp.entity.Account;
//import com.example.bankingapp.entity.AccountStatus;
//import com.example.bankingapp.entity.Role;
//import com.example.bankingapp.entity.User;
//import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
//import com.example.bankingapp.repository.AccountRepository;
//import com.example.bankingapp.repository.UserRepository;
//import com.example.bankingapp.service.AccountService;
//import com.example.bankingapp.service.UserService;
//import com.example.bankingapp.validation.ValidationService;
//import org.checkerframework.checker.units.qual.A;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class AccountServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @InjectMocks
//    private AccountService accountService;
//    @Mock
//    private UserService userService;
//    @Mock
//    private ValidationService validationService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateAccount_Success() throws Exception {
//        UUID userId = UUID.randomUUID();
//        User user = User.builder()
//                .id(userId)
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .role(Role.ADMIN)
//                .build();
////        UserDTO userDTO = new UserDTO();
////        userDTO.setAge(22);
////        userDTO.setRole(Role.ADMIN);
////        userDTO.setEmail("manan@gmail.com");
////        userDTO.setFirstName("manan");
////        userDTO.setLastName("matta");
////        userDTO.setPassword("manan");
//
////        ResponseUserDTO user = userService.add(userDTO , any() , "POST");
////        UUID userId = user.getId();
//
//        String token = "validToken";
//        when(validationService.returnRole(token)).thenReturn("ADMIN");
//        when(userRepository.findById(any())).thenReturn(Optional.of(user));
//        Optional<User> findByID = userRepository.findById(userId);
//        AccountDTO accountDTO = new AccountDTO();
//        accountDTO.setCurrency("USD");
//        accountDTO.setAccountStatus(AccountStatus.ACTIVE);
//        accountDTO.setBalance(1000);
//        accountDTO.setName("SAVINGS");
//
//
//        Account account = new Account();
//        account.setName(accountDTO.getName());
//        account.setBalance(accountDTO.getBalance());
//        account.setCurrency(accountDTO.getCurrency());
//        account.setAccountStatus(accountDTO.getAccountStatus());
//        account.setUser(user);
//        when(accountRepository.save(account)).thenReturn(account);
//
//        AccountDTO expectedAccountDTO = new AccountDTO();
//        expectedAccountDTO.setName(account.getName());
//        expectedAccountDTO.setBalance(account.getBalance());
//        expectedAccountDTO.setCurrency(account.getCurrency());
//        expectedAccountDTO.setAccountStatus(account.getAccountStatus());
//
//        AccountDTO result = accountService.createaccount(userId , accountDTO , token);
//
//        assertNotNull(result);
//        assertEquals(expectedAccountDTO, result);
//
//    }
//
////    @Test
////    public void testCreateAccount_UserNotFound() {
////
////        AccountDTO accountDTO = new AccountDTO();
////        accountDTO.setBalance(1000);
////        accountDTO.setAccountStatus(AccountStatus.ACTIVE);
////        accountDTO.setName("SAVINGS");
////        accountDTO.setCurrency("USD");
////
////        UUID userId = UUID.randomUUID();
////        Account account = new Account();
////        account.setName(accountDTO.getName());
////        account.setBalance(accountDTO.getBalance());
////        account.setCurrency(accountDTO.getCurrency());
////        account.setAccountStatus(accountDTO.getAccountStatus());
////        String token = "validate token";
//////       when(validationService.validateToken(any() , any())).thenReturn(true);
////
////
////
////        UserNotFoundExcetion exception = assertThrows(.class, () -> {
////            accountService.createaccount(userId , accountDTO , token);
////        });
////
////        assertEquals("User Not Found at given id : "+userId, exception.getMessage());
////
////    }
//}
