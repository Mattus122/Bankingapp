package com.example.bankingapp.service;

import com.example.bankingapp.controller.UserController;
import com.example.bankingapp.dto.*;
import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import com.example.bankingapp.exception.UserNotFoundException;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.timedinterface.Timed;
import com.example.bankingapp.validation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserValidationService userValidationService;
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Timed
    public ResponseUserDTO add(UserDTO userDTO,String token){
        Optional<User> findUser = userRepository.findByEmail(userDTO.getEmail());
        userValidationService.validatePostCreation(token,userDTO);
        if(findUser.isPresent()){
            throw new UserAlreadyExistsException("User Already present in database");
        }
        User newUser = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .role(userDTO.getRole())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .age(userDTO.getAge())
                .organisationName(userDTO.getOrganisationName())
                .build();
        User savedUser = userRepository.save(newUser);
        return convertToDTO(savedUser);
    }
    @Timed
    public ResponseUserDTO updateUser(String token,UUID userId,UpdateUserDTO updateUserDTO){
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: "+userId));
        userValidationService.validateUpdation(token,existingUser);
        existingUser.setFirstName(updateUserDTO.getFirstName());
        existingUser.setLastName(updateUserDTO.getLastName());
        existingUser.setPassword(updateUserDTO.getPassword());
        existingUser.setAge(updateUserDTO.getAge());
        User updatedUser = userRepository.save(existingUser);
        return convertToDTO(updatedUser);
    }
    @Timed
    public void deleteUserById(UUID userId,String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        userValidationService.validateDeletion(token,user);
        userRepository.deleteById(userId);
    }
    @Timed
    public UserDetailsDTO findUserById(UUID userId,String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("no user found"));
        userValidationService.getUserById(token,user);
        return UserDetailsDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .userId(user.getId())
                .age(user.getAge())
                .role(user.getRole())
                .organisationName(user.getOrganisationName())
                .accountList(user.getAccounts())
                .build();
    }
    public List<UserDetailsDTO> allUserByOrganisationName(String token){
        OrganisationName organisationNameFromToken = userValidationService.validateGetAllUserByOrgName(token);
        List<User> usersList = new ArrayList<>();
        for(User u : userRepository.findAll()){
            if(u.getOrganisationName().equals(organisationNameFromToken)
                    &&(u.getRole().equals(Role.BANKADMIN)
                    ||u .getRole().equals(Role.USER))){usersList.add(u);}
        }
        return usersList.stream()
                    .map(this::convertToUserDetailsDTO)
                    .collect(Collectors.toList());
    }
    private ResponseUserDTO convertToDTO(User user) {
        ResponseUserDTO responseUserDTO = new ResponseUserDTO();
        responseUserDTO.setOrganisationName(user.getOrganisationName());
        responseUserDTO.setCreationTimeStamp(user.getCreationTimeStamp());
        responseUserDTO.setId(user.getId());
        responseUserDTO.setRole(user.getRole());
        responseUserDTO.setEmail(user.getEmail());
        responseUserDTO.setFirstName(user.getFirstName());
        responseUserDTO.setLastName(user.getLastName());
        return responseUserDTO;
    }
    private UserDetailsDTO convertToUserDetailsDTO(User user){
        return UserDetailsDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .userId(user.getId())
                .age(user.getAge())
                .role(user.getRole())
                .organisationName(user.getOrganisationName())
                .accountList(user.getAccounts())
                .build();
    }
    public List<User> filterUserByParameters(String token, OrganisationName organisationName
            ,Date startDate,Date endDate,String firstName){
        UserFilterDTO userFilterDTO=userValidationService.validateFilterUser(token,organisationName,startDate,endDate,firstName);
        return userRepository.findUsersByParams(userFilterDTO.getOrganisationName()
                                                ,userFilterDTO.getStartDate(),userFilterDTO.getEndDate()
                                                ,userFilterDTO.getFirstName());
    }
}
