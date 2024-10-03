package com.example.bankingapp.controller;

import com.example.bankingapp.aspect.RoleCheck;
import com.example.bankingapp.dto.*;
import com.example.bankingapp.entity.OrganisationName;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "User",description = "User Management Api ")
public class UserController {
    private final UserService userService;
    @PostMapping("/user")
    @RoleCheck(roles = {Role.SUPERADMIN,Role.BANKADMIN})
    ResponseEntity<ResponseUserDTO> adduser(@RequestBody @Valid UserDTO userDTO,
                                            @RequestHeader("Authorization")String token) {
        return new ResponseEntity<>(userService.add(userDTO,token),HttpStatus.CREATED);
    }
    @GetMapping("/filter")
    @RoleCheck(roles = {Role.SUPERADMIN, Role.BANKADMIN})
    public ResponseEntity<List<User>> filterUsers(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "organisationName", required = false) OrganisationName organisationName,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(value = "firstName", required = false) String firstName) {

        List<User> users = userService.filterUserByParameters(token, organisationName, startDate, endDate, firstName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/allUser")
    @RoleCheck(roles = {Role.SUPERADMIN,Role.BANKADMIN})
    ResponseEntity<List<UserDetailsDTO>> getAllUsers(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(userService.allUserByOrganisationName(token),HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    ResponseEntity<UserDetailsDTO> fetchUserById(@PathVariable UUID userId,
                                                  @RequestHeader("Authorization")String token){
        UserDetailsDTO userDetailsDTO = userService.findUserById(userId,token);
        return new ResponseEntity<>(userDetailsDTO,HttpStatus.FOUND);
    }
    @PutMapping("/user/{userId}")
    @RoleCheck(roles = {Role.SUPERADMIN,Role.BANKADMIN})
    ResponseEntity<ResponseUserDTO> updateUser(@PathVariable UUID userId,
                                               @RequestBody @Valid UpdateUserDTO updateUserDTO,
                                               @RequestHeader("Authorization")String token){
        ResponseUserDTO updatedUser = userService.updateUser(token,userId,updateUserDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @DeleteMapping("/user/{userId}")
    @RoleCheck(roles = {Role.SUPERADMIN,Role.BANKADMIN})
    ResponseEntity<Void> deleteUserById(@PathVariable UUID userId,
                                        @RequestHeader("Authorization")String token){
        userService.deleteUserById(userId, token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
