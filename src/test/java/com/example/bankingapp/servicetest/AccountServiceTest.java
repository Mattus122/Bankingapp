package com.example.bankingapp.servicetest;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.AccountStatus;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.accountexception.AccounrCreationException;
import com.example.bankingapp.exception.jwtExcetion.ForbiddenRequestException;
import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.AccountService;
import com.example.bankingapp.validation.ValidationService;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.boot.model.process.internal.UserTypeResolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private AccountService accountService;

    private UUID userId;
    private AccountDTO accountDTO;
    private User user;
    private String token;
    private UUID accountID;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        accountID = UUID.randomUUID();
        token = "Bearer some-token";

        accountDTO = new AccountDTO();
        accountDTO.setAccountId(accountID);
        accountDTO.setName("Test Account");
        accountDTO.setBalance(1000);
        accountDTO.setCurrency("USD");
        accountDTO.setAccountStatus(AccountStatus.ACTIVE);

        user = new User();
        user.setId(userId);
        user.setEmail("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
        user.setRole(Role.USER);
        user.setAge(22);
    }

    @Test
    void testCreateAccount_Success() throws Exception {
        when(validationService.returnRole(anyString())).thenReturn("ADMIN");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(new Account());


        AccountDTO result = accountService.createAccount(userId, accountDTO, token);

        assertNotNull(result);
//        assertEquals(result , accountDTO);
//        verify(userRepository, times(1)).findById(userId);
//        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_UserRoleForbidden() {
        when(validationService.returnRole(anyString())).thenReturn("USER");

        ForbiddenRequestException exception = assertThrows(ForbiddenRequestException.class, () ->
                accountService.createAccount(userId, accountDTO, token)
        );

        assertEquals("Access denied for the desired operation", exception.getMessage());
    }

    @Test
    void testCreateAccount_AdminCannotCreateAccount() {
        user.setRole(Role.ADMIN);
        when(validationService.returnRole(anyString())).thenReturn("ADMIN");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ForbiddenRequestException exception = assertThrows(ForbiddenRequestException.class, () ->
                accountService.createAccount(userId, accountDTO, token)
        );

        assertEquals("Admin cannot create an account for themselves", exception.getMessage());
    }

    @Test
    void testCreateAccount_UserNotFound() {
        when(validationService.returnRole(anyString())).thenReturn("ADMIN");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundExcetion exception = assertThrows(UserNotFoundExcetion.class, () ->
                accountService.createAccount(userId, accountDTO, token)
        );

        assertEquals("No user found for ID: " + userId, exception.getMessage());
    }
}