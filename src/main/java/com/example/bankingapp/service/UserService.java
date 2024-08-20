package com.example.bankingapp.service;

import com.example.bankingapp.controller.UserController;
import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.dto.JwtTokenResponse;
import com.example.bankingapp.dto.ResponseUserDTO;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.jwtExcetion.ForbiddenRequestException;
import com.example.bankingapp.exception.jwtExcetion.InvalidJwtToken;
import com.example.bankingapp.exception.userexception.UserAlreadyExistsException;
import com.example.bankingapp.exception.userexception.UserNotFoundExcetion;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.TransactionRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.timedinterface.Timed;
import com.example.bankingapp.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ValidationService validationService;
    private final TransactionRepository transactionRepository;
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Timed
    public ResponseUserDTO add(UserDTO userDTO, String token , String requestType) throws Exception {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.validateToken(token , "POST")){
            User newUser  = User.builder().firstName(userDTO.getFirstName()).lastName(userDTO.getLastName())
                    .role(userDTO.getRole()).email(userDTO.getEmail()).password(userDTO.getPassword()).age(userDTO.getAge())
                    .build();
            String email = newUser.getEmail();
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                throw new UserAlreadyExistsException("User already exists in the database.");
            }
            User savedUser = userRepository.save(newUser);
            return convertToDTO(newUser);

        }
        return null;
    }

    @Timed
    public List<ResponseUserDTO> returnAllUser(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.validateToken(token , "GET") && validationService.returnRole(token).equals("ADMIN")){
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::convertToDTO )
                    .collect(Collectors.toList());
        } else  {
            List<ResponseUserDTO> responseUserDTOList = new ArrayList<>();
            String email = validationService.getEmailFromToken(token);
            Optional<User> user= userRepository.findByEmail(email);
            ResponseUserDTO responseUserDTO = convertToDTO(user.get());
            responseUserDTOList.add(responseUserDTO);
            return responseUserDTOList;
        }
    }

    @Timed
    public ResponseUserDTO updateUser(UUID userId, UserDTO userDTO , String token , String requestType) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.validateToken(token , "PUT")){
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setPassword(userDTO.getPassword());
                user.setRole(userDTO.getRole());
                user.setAge(userDTO.getAge());
                User updatedUser = userRepository.save(user);
                return convertToDTO(updatedUser);
            } else {
                throw new UserNotFoundExcetion("User not found with id: " + userId);
            }
        }
        throw new InvalidJwtToken("Pata nhi kya hua ");


    }
    @Timed
    public void deleteUserById(UUID userId , String token  , String requestType) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.validateToken(token , "DELETE")){
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
            } else {
                throw new UserNotFoundExcetion("User not found with id: " + userId);
            }
        }

    }
    @Timed
    public ResponseUserDTO findUserById(UUID userId , String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if(validationService.returnRole(token).equals("USER")){
            String email = validationService.getEmailFromToken(token);
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.get().getId().equals(userId)){
                ResponseUserDTO responseUserDTO = convertToDTO(optionalUser.get());
                return responseUserDTO;
            }
            throw new ForbiddenRequestException("Cannot Access other Users Information");

        } else if (validationService.returnRole(token).equals("ADMIN")) {
            Optional<User> byId = userRepository.findById(userId);
            if(byId.isPresent()){
                ResponseUserDTO responseUserDTO = convertToDTO(byId.get());
                return responseUserDTO;
            }
            else{
                throw new UserNotFoundExcetion("No user found at gven id : "+userId);
            }
        }
        return null;

    }
    @Timed
    private ResponseUserDTO convertToDTO(User user) {
        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
        responseUserDTO.setId(user.getId());
        responseUserDTO.setRole(user.getRole());
        responseUserDTO.setEmail(user.getEmail());
        responseUserDTO.setFirstName(user.getFirstName());
        responseUserDTO.setLastName(user.getLastName());
        return responseUserDTO;
    }
    @Timed
    public JwtTokenResponse createJwtToken(JwtTokenDTO jwtTokenDTO ) {
        String jwt = jwtService.generateToken(jwtTokenDTO);
        return JwtTokenResponse.builder().accessToken(jwt).build();
    }
}
