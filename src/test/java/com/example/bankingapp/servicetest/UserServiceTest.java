package com.example.bankingapp.servicetest;

import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import com.example.bankingapp.exception.UserNotFoundExcetion;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testadd() {
        User user  = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id( UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        String email = "manan@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        userService.add(user);


    }
    @Test
    public void  testaddunsucessful(){
        User user  = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id( UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        String email = "manan@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

//        assertThrows(new UserAlreadyExistsException("user exsists in database "));
        Throwable exception = assertThrows(UserAlreadyExistsException.class,
                ()->{userService.add(user); });
        Assertions.assertEquals(exception.getMessage() , "User Already Exists in Database ");

    }

    @Test
    public void testdeletesuccesfuly(){

        User user  = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id( UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        userService.delete(id);

    }
    @Test
    public void testdeleteuserunsuccessful(){
        User user  = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id( UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(UserNotFoundExcetion.class,
                ()->{userService.delete(id); });
        Assertions.assertEquals(exception.getMessage() , "Cannot find User at id : "+id);

    }

    @Test
    public void testgetUserByidSuccessfull() {
        User user = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id(UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        userService.findById(id);

    }
    @Test
    public void  testgetUserByIdunsuccessful(){
        User user = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id(UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        UUID id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(UserNotFoundExcetion.class,
                ()->{userService.findById(id); });
        Assertions.assertEquals(exception.getMessage() , "No user found at gven id : "+id);


    }

    @Test
    public void testgetAllUSersuccessful(){
        User user1 = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id(UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        User user2 = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id(UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
        when(userRepository.findAll()).thenReturn(List.of(user1,user2));
    }


}
