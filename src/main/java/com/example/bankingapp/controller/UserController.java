package com.example.bankingapp.controller;

import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.dto.JwtTokenResponse;
import com.example.bankingapp.dto.ResponseUserDTO;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor

@Tag(name = "User" , description = "User Management Api ")
public class UserController {
    private final UserService userService;
    @PostMapping("/user")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody @Valid UserDTO userDTO, @RequestHeader("Authorization") String token ) throws Exception {
        ResponseUserDTO createdUser  = userService.add(userDTO, token , "POST");
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @PostMapping("/user/generateToken")
    public ResponseEntity<JwtTokenResponse> generateJwtToken(@RequestBody JwtTokenDTO jwtTokenDTO){
        JwtTokenResponse jwtTokenResponse = userService.createJwtToken(jwtTokenDTO );
        return new ResponseEntity<>(jwtTokenResponse, HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers(@RequestHeader("Authorization") String token) {
        List<ResponseUserDTO> responseUserDTOList = userService.returnAllUser(token);
        if (responseUserDTOList.isEmpty()) {
            return new ResponseEntity<>(responseUserDTOList, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseUserDTOList, HttpStatus.OK);
    }
    @GetMapping ("/user/{userId}")
    ResponseEntity<ResponseUserDTO> findUserById(@PathVariable UUID userId , @RequestHeader("Authorization") String token){
        ResponseUserDTO user = userService.findUserById(userId , token);
        return new ResponseEntity<>(user , HttpStatus.FOUND);
    }
    @PutMapping("/user/{userId}")
    public ResponseEntity<ResponseUserDTO> updateUser(@RequestBody @Valid UserDTO userDTO , @PathVariable UUID userId , @RequestHeader("Authorization") String token) {
        ResponseUserDTO updatedUser = userService.updateUser(userId, userDTO , token ,"PUT");
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID userId , @RequestHeader("Authorization") String token) {
        userService.deleteUserById(userId , token , "DELETE");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
