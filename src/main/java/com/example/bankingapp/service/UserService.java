package com.example.bankingapp.service;

import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.exception.UserAlreadyExistsException;
import com.example.bankingapp.exception.UserNotFoundExcetion;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.timedinterface.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Timed
    public ResponseEntity<UserDTO> add(User user) {
        User newUser = User.builder().firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail())
                .password(user.getPassword()).dob(user.getDob()).build();
        String mail = user.getEmail();
        Optional<User> findBymail = userRepository.findByEmail(mail);
        if(!findBymail.isPresent()){
            UserDTO u1 = convertToDTO(user);
            User savedUser  = userRepository.save(newUser);
            return new ResponseEntity<>( u1 , HttpStatus.CREATED);
        }else{
            throw new UserAlreadyExistsException("User Already Exists in Database ");
        }

    }
    @Timed
    public ResponseEntity<List<User>> returnAllUser(){
        List<User> userlist = new ArrayList<User>();
        userRepository.findAll().forEach(userlist::add);
        if(userlist.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userlist,HttpStatus.OK);
        // try using Dto so you donot return the password.
//        return (ResponseEntity<List<User>>) userlist.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());

    }
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDob(user.getDob());
        return userDTO;
    }
    @Timed

    public ResponseEntity<UserDTO> update(User newUserData, UUID userId) {
        Optional<User> oldUserData = userRepository.findById(userId);

            if(oldUserData.isPresent()){
                User updateuser = oldUserData.get();
                updateuser.setFirstName(newUserData.getFirstName());
                updateuser.setLastName(newUserData.getLastName());
                updateuser.setEmail(newUserData.getEmail());
                updateuser.setDob(newUserData.getDob());
                userRepository.save(updateuser);
                UserDTO u = convertToDTO(updateuser);
                return new ResponseEntity<>(u  , HttpStatus.OK);

            }
            else {
                throw new UserNotFoundExcetion("Cannot Find user at given UUID" + userId);
            }



    }
    @Timed

    public ResponseEntity<HttpStatus> delete(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        if(byId.isPresent()){
            userRepository.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            throw new UserNotFoundExcetion("Cannot find User at id : "+userId);
        }
    }

    public ResponseEntity<UserDTO> findById(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        if(byId.isPresent()){
            UserDTO userDTO = convertToDTO(byId.get());
            return new ResponseEntity<>(userDTO , HttpStatus.FOUND);

        }
        else{
            throw new UserNotFoundExcetion("No user found at gven id : "+userId);
        }
    }
}
