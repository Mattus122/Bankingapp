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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

//    @Test
//    public void testAdd() {
//        User user = User.builder()
//                .email("manan@gmail.com")
//                .firstName("manan")
//                .lastName("matta")
//                .id(UUID.randomUUID())
//                .dob(LocalDate.of(1990, 1, 1))
//                .build();
//
//        UserDTO expectedUserDTO = new UserDTO();
//        expectedUserDTO.setFirstName("manan");
//        expectedUserDTO.setLastName("matta");
//        expectedUserDTO.setDob(LocalDate.of(1990, 1, 1));
//
//        String email = "manan@gmail.com";
//        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
//        when(userRepository.save(any())).thenReturn(user);
//
//        UserDTO response = userService.add(user);
//
//        assertEquals(expectedUserDTO.getFirstName(), response.getFirstName());
//        assertEquals(expectedUserDTO.getLastName(), response.getLastName());
//        assertEquals(expectedUserDTO.getDob(), response.getDob());
//    }
//@Test
//public void testAddUser_Success() {
//    User user = User.builder()
//            .firstName("manan")
//            .lastName("matta")
//            .email("manan@gmail.com")
//            .password("manan")
//            .dob(LocalDate.of(2002, 5, 1))
//            .build();
//
//    UserDTO expectedUserDTO = new UserDTO();
//    expectedUserDTO.setFirstName("manan");
//    expectedUserDTO.setLastName("matta");
//    expectedUserDTO.setDob(LocalDate.of(2002, 5, 1));
//
//    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
////    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
//    when(userRepository.save(any())).thenReturn(expectedUserDTO);
//    UserDTO result  = userService.add(user);
//    assertNotNull(result);
//    assertEquals(expectedUserDTO, result);
//    verify(userRepository , times(1)).findByEmail(user.getEmail());
//    verify(userRepository,times(1)).save(user);
//}
@Test
public void testAddUser_Success() {
    User user = User.builder()
            .firstName("manan")
            .lastName("matta")
            .email("manan@gmai.com")
            .password("manan")
            .dob(LocalDate.of(2002, 5, 1))
            .build();


    UserDTO expectedUserDTO = new UserDTO();
    expectedUserDTO.setFirstName("manan");
    expectedUserDTO.setLastName("matta");
    expectedUserDTO.setDob(LocalDate.of(2002, 5, 1));

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(any())).thenReturn(user);

    UserDTO result = userService.add(user);
    assertNotNull(result);

    assertEquals(expectedUserDTO, result);
    verify(userRepository , times(1)).findByEmail(user.getEmail());
    verify(userRepository,times(1)).save(user);
}

    @Test
    public void testAddUser_UserAlreadyExists() {
        User user = User.builder()
                .firstName("manan")
                .lastName("matta")
                .email("manan@gmail.com")
                .password("manan")
                .dob(LocalDate.of(2002, 5, 1))
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

//        assertThrows(UserAlreadyExistsException.class, () -> {
//            userService.add(user);
//        });
        Throwable exception = assertThrows(UserAlreadyExistsException.class,
                ()->{userService.add(user); });
        assertEquals(exception.getMessage() , "User already exists in the database.");
    }
//
//    @Test
//    public void testdeletesuccesfuly(){
//
//        User user  = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id( UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
//        UUID id = user.getId();
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//        userService.deleteUserById(id);
//
//    }
//    @Test
//    public void testdeleteuserunsuccessful(){
//        User user  = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id( UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
//        UUID id = user.getId();
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//        Throwable exception = assertThrows(UserNotFoundExcetion.class,
//                ()->{userService.deleteUserById(id); });
//        assertEquals(exception.getMessage() , "Cannot find User at id : "+id);
//
//    }
@Test
public void testDeleteUser_Success() {
    UUID userId = UUID.randomUUID();
    User user = new User();
    user.setId(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    doNothing().when(userRepository).deleteById(userId);

    userService.deleteUserById(userId);

    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).deleteById(userId);
}

    @Test
    public void testDeleteUser_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(false);

        Throwable exception = assertThrows(UserNotFoundExcetion.class,
                ()->{userService.deleteUserById(userId); });
        assertEquals(exception.getMessage() , "User not found with id: " + userId);
    }


//    @Test
//    public void testgetUserByidSuccessfull() {
//        User user = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id(UUID.randomUUID()).dob(LocalDate.of(2002, 5, 1)).build();
//        UUID id = user.getId();
//        UserDTO expectedUserDTO = new UserDTO();
//        expectedUserDTO.setFirstName("manan");
//        expectedUserDTO.setLastName("matta");
//        expectedUserDTO.setDob(LocalDate.of(2002, 5, 1));
//        when(userRepository.findById(any())).thenReturn(user);
//        UserDTO actual = userService.findUserById(id);
//        assertEquals(actual , expectedUserDTO);
//    }
//    @Test
//    public void  testgetUserByIdunsuccessful(){
//        User user = User.builder().email("manan@gmail.com").firstName("manan").lastName("matta").id(UUID.randomUUID()).dob(LocalDate.of(1990, 1, 1)).build();
//        UUID id = user.getId();
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//        Throwable exception = assertThrows(UserNotFoundExcetion.class,
//                ()->{userService.findUserById(id); });
//        assertEquals(exception.getMessage() , "No user found at gven id : "+id);
//
//
//    }

    @Test
    public void testFindUserById_Success() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .firstName("manan")
                .lastName("matta")
                .dob(LocalDate.of(2002, 5, 1))
                .email("manan@gmail.com")
                .build();

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setFirstName("manan");
        expectedUserDTO.setLastName("matta");
        expectedUserDTO.setDob(LocalDate.of(2002, 5, 1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.findUserById(userId);

        assertEquals(expectedUserDTO, result);
    }

    @Test
    public void testFindUserById_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExcetion.class, () -> {
            userService.findUserById(userId);
        });

        verify(userRepository, times(1)).findById(userId);

    }

    @Test
    public void testReturnAllUser_Success() {
        User user1 = User.builder()
                .id(UUID.randomUUID())
                .firstName("manan")
                .lastName("matta")
                .dob(LocalDate.of(2002, 5, 1))
                .email("manan@gmail.com")
                .build();

        User user2 = User.builder()
                .id(UUID.randomUUID())
                .firstName("panav")
                .lastName("yadav")
                .dob(LocalDate.of(1992, 2, 2))
                .email("panav@gmail.com")
                .build();

        List<User> userList = Arrays.asList(user1, user2);

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setFirstName("manan");
        userDTO1.setLastName("matta");
        userDTO1.setDob(LocalDate.of(2002, 5, 1));

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setFirstName("panav");
        userDTO2.setLastName("yadav");
        userDTO2.setDob(LocalDate.of(1992, 2, 2));

        List<UserDTO> expectedUserDTOList = Arrays.asList(userDTO1, userDTO2);

        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> result = userService.returnAllUser();

        assertEquals(expectedUserDTOList, result);
    }

    @Test
    public void testReturnAllUser_NoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> result = userService.returnAllUser();

        assertEquals(Collections.emptyList(), result);
    }
    @Test
    public void testUpdateUser_Success() {
        UUID userId = UUID.randomUUID();
        User existingUser = User.builder()
                .id(userId)
                .firstName("manan")
                .lastName("matta")
                .dob(LocalDate.of(2002, 5, 1))
                .email("manan@gmail.com")
                .password("password123")
                .build();

        User updateUser = User.builder()
                .firstName("panav")
                .lastName("yadav")
                .dob(LocalDate.of(1992, 2, 2))
                .email("panav@gmail.com")
                .password("newpassword123")
                .build();


        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setFirstName("panav");
        expectedUserDTO.setLastName("yadav");
        expectedUserDTO.setDob(LocalDate.of(1992, 2, 2));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(updateUser);

        UserDTO result = userService.updateUser(userId, updateUser);

        assertEquals(expectedUserDTO, result);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        User updateUser = User.builder()
                .firstName("panav")
                .lastName("yadav")
                .dob(LocalDate.of(1992, 2, 2))
                .email("panav@gmail.com")
                .password("newpassword123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundExcetion.class, () -> {
            userService.updateUser(userId, updateUser);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any());
    }
}




