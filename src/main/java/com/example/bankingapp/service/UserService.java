package com.example.bankingapp.service;

import com.example.bankingapp.dto.JwtTokenResponse;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import com.example.bankingapp.exception.UserNotFoundExcetion;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.timedinterface.Timed;
import com.example.bankingapp.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ValidationService validationService;

    @Timed
    public UserDTO add(User user , String token) throws Exception {
//        User newUser  = User.builder().firstName(user.getFirstName()).email(user.getEmail()).password(user.getPassword())
//                .dob(user.getDob()).build();
        validationService.validateToken(token);
        String email = user.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists in the database.");
        }
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Timed
    public List<UserDTO> returnAllUser(String token) {
        validationService.validateToken(token);
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Timed
    public UserDTO updateUser(UUID userId, User newUserData) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(newUserData.getFirstName());
            user.setLastName(newUserData.getLastName());
            user.setEmail(newUserData.getEmail());
            user.setDob(newUserData.getDob());
            user.setPassword(newUserData.getPassword());
            User updatedUser = userRepository.save(user);
            return convertToDTO(updatedUser);
        } else {
            throw new UserNotFoundExcetion("User not found with id: " + userId);
        }
    }
//
//    public UserDTO update(User newUserData, UUID userId) {
//        Optional<User> oldUserData = userRepository.findById(userId);
//
//            if(oldUserData.isPresent()){
//                User updateuser = oldUserData.get();
//                updateuser.setFirstName(newUserData.getFirstName());
//                updateuser.setLastName(newUserData.getLastName());
//                updateuser.setEmail(newUserData.getEmail());
//                updateuser.setDob(newUserData.getDob());
//                userRepository.save(updateuser);
//                UserDTO u = convertToDTO(updateuser);
//                return new ResponseEntity<>(u  , HttpStatus.OK);
//
//            }
//            else {
//                throw new UserNotFoundExcetion("Cannot Find user at given UUID" + userId);
//            }
//
//
//
//    }
    @Timed
    public void deleteUserById(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundExcetion("User not found with id: " + userId);
        }
    }
    public UserDTO findUserById(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        if(byId.isPresent()){
            UserDTO userDTO = convertToDTO(byId.get());
            return userDTO ;
        }
        else{
            throw new UserNotFoundExcetion("No user found at gven id : "+userId);
        }
    }
    private  UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDob(user.getDob());
        userDTO.setAge(user.getAge());
        return userDTO;
    }
    public JwtTokenResponse generateToken(User user) {
        String token = jwtService.generateToken(user);
        return JwtTokenResponse.builder().accessToken(token).build();
    }
}
