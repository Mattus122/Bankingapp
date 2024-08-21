package com.example.bankingapp.servicetest;

import com.example.bankingapp.dto.ResponseUserDTO;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.userexception.UserAlreadyExistsException;
import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.validation.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    @Mock
    private ValidationService validationService;



@Test
public void testAddUser_Success() throws Exception {

    UserDTO userDTO = new UserDTO();
    userDTO.setAge(22);
    userDTO.setRole(Role.ADMIN);
    userDTO.setEmail("manan@gmail.com");
    userDTO.setFirstName("manan");
    userDTO.setLastName("matta");
    userDTO.setPassword("manan");

    User user = User.builder()
            .firstName(userDTO.getFirstName())
            .lastName(userDTO.getLastName())
            .email(userDTO.getEmail())
            .password(userDTO.getPassword())
            .age(userDTO.getAge())
            .role(userDTO.getRole())
            .build();


    ResponseUserDTO responseUserDTO = new ResponseUserDTO();
    responseUserDTO.setId(user.getId());
    responseUserDTO.setRole(user.getRole());
    responseUserDTO.setEmail(user.getEmail());
    responseUserDTO.setFirstName(user.getFirstName());
    responseUserDTO.setLastName(user.getLastName());
    String token  = "validtoken";
    when(validationService.validateToken(any() ,any()) && !validationService.isTokenExpired(any())).thenReturn(true);
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(any())).thenReturn(user);
    ResponseUserDTO result = userService.add(userDTO , token , "POST");
    assertNotNull(result);
    assertEquals(responseUserDTO, result);
    verify(userRepository , times(1)).findByEmail(user.getEmail());
    verify(userRepository,times(1)).save(user);
}

    @Test
    public void testAddUser_UserAlreadyExists() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setAge(22);
        userDTO.setRole(Role.ADMIN);
        userDTO.setEmail("manan@gmail.com");
        userDTO.setFirstName("manan");
        userDTO.setLastName("matta");
        userDTO.setPassword("manan");
        User user = User.builder()
                .firstName("manan")
                .lastName("matta")
                .email("manan@gmail.com")
                .password("manan")
                .build();
        String token = "ValidationToken";
        when(validationService.validateToken(any() , any()) && !validationService.isTokenExpired(any())).thenReturn(true);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));


//        assertThrows(UserAlreadyExistsException.class, () -> {
//            userService.add(user);
//        });
        Throwable exception = assertThrows(UserAlreadyExistsException.class,
                ()->{userService.add(userDTO , token , "POST" ); });
        assertEquals(exception.getMessage() , "User already exists in the database.");
    }

    @Test
    public void testDeleteUserById_UserExists() {
        UUID userId = UUID.randomUUID();
        String token = "ValidationToken";
        when(validationService.validateToken(any() , any()) && !validationService.isTokenExpired(any())).thenReturn(true);
        // Mock behavior: user exists
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // Call the method to be tested
        userService.deleteUserById(userId , token , "DELETE");

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        String token = "ValidationToken";
        when(validationService.validateToken(any() , any()) && !validationService.isTokenExpired(any())).thenReturn(true);


        when(userRepository.existsById(userId)).thenReturn(false);

        Throwable exception = assertThrows(UserNotFoundExcetion.class,
                ()->{userService.deleteUserById(userId , token , "DELETE"); });
        assertEquals(exception.getMessage() , "User not found with id: " + userId);
    }

@Test
public void testDeleteUserById_UserDoesNotExist() {
    UUID userId = UUID.randomUUID();
    String token = "ValidationToken";
    when(validationService.validateToken(any() , any()) && !validationService.isTokenExpired(any())).thenReturn(true);

    // Mock behavior: user does not exist
    when(userRepository.existsById(userId)).thenReturn(false);

    // Call the method and assert that the exception is thrown
    assertThrows(UserNotFoundExcetion.class, () -> userService.deleteUserById(userId , token , "DELETE"));


    // Verify interactions with the mock
    verify(userRepository).existsById(userId);
}
//
//


//
//

    @Test
    public void testUpdateUser_UserNotFound() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAge(22);
        userDTO.setRole(Role.ADMIN);
        userDTO.setEmail("manan@gmail.com");
        userDTO.setFirstName("manan");
        userDTO.setLastName("matta");
        userDTO.setPassword("manan");

        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .age(userDTO.getAge())
                .role(userDTO.getRole())
                .build();
        UUID userId = user.getId();


        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
        responseUserDTO.setId(user.getId());
        responseUserDTO.setRole(user.getRole());
        responseUserDTO.setEmail(user.getEmail());
        responseUserDTO.setFirstName(user.getFirstName());
        responseUserDTO.setLastName(user.getLastName());
        String token  = "validtoken";
        when(validationService.validateToken(any() ,any()) && !validationService.isTokenExpired(any())).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExcetion.class, () -> {
            userService.updateUser(userId, userDTO , token , "PUT");
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any());
    }
}




