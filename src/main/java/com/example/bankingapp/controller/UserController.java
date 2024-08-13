package com.example.bankingapp.controller;

import com.example.bankingapp.dto.JwtTokenDTO;
import com.example.bankingapp.dto.JwtTokenResponse;
import com.example.bankingapp.dto.ResponseUserDTO;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.service.TransactionService;
import com.example.bankingapp.service.UserService;
import com.example.bankingapp.validation.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final TransactionService transactionService;
    private final ValidationService validationService;
    Logger logger = LoggerFactory.getLogger(UserController.class);


    @Operation(
            summary = "Create a User",
            description = "Create a User Object. The response is a UserDTO containing firstname, lastname, and dob."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })

    @PostMapping("/user")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody @Valid UserDTO userDTO, @RequestHeader("Authorization") String token ) throws Exception {
        ResponseUserDTO createdUser  = userService.add(userDTO, token , "POST");
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @Operation(
            summary = "Create a token for user",
            description = "Create a Jwt toke  Object. The response is a Jwt token containing subject as email and claims as firstname, lastname ."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Token created successfully", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = { @Content(schema = @Schema()) }),
    })
    @PostMapping("/user/generateToken")
    public ResponseEntity<JwtTokenResponse> generateJwtToken(@RequestBody JwtTokenDTO jwtTokenDTO){
        JwtTokenResponse jwtTokenResponse = userService.generateToken(jwtTokenDTO );
        return new ResponseEntity<>(jwtTokenResponse, HttpStatus.OK);
    }
    @Operation(
            summary = "Retrieve all users",
            description = "Fetches a list of all users in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/user")
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers(@RequestHeader("Authorization") String token) {
        List<ResponseUserDTO> responseUserDTOList = userService.returnAllUser(token);
        if (responseUserDTOList.isEmpty()) {
            return new ResponseEntity<>(responseUserDTOList, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseUserDTOList, HttpStatus.OK);
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }

//        if (validationService.validateRole(token , "GET") && !validationService.isTokenExpired(token)) {
//            List<ResponseUserDTO> responseUserDTOList = userService.returnAllUser();
//             if (responseUserDTOList.isEmpty()) {
//                return new ResponseEntity<>(responseUserDTOList, HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(responseUserDTOList, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
    }
    @Operation(
            summary = "Find User by ID",
            description = "Retrieve a User Object by ID. The response is a UserDTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "User not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
    @GetMapping ("/user/{userId}")
    ResponseEntity<ResponseUserDTO> findUserById(@PathVariable UUID userId , @RequestHeader("Authorization") String token){
        ResponseUserDTO user = userService.findUserById(userId , token);
        return new ResponseEntity<>(user , HttpStatus.FOUND);
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }

//        if(!validationService.isTokenExpired(token) && validationService.validateRole(token , "GET")){
//            ResponseUserDTO user = userService.findUserById(userId);
//            return new ResponseEntity<>(user , HttpStatus.FOUND);
//        }
//        else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
    }
    @Operation(
            summary = "Update a user by id",
            description = "Fetches a list of all users in the systemand then find the user by id and updates it."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users Updated successfully", content = { @Content(schema = @Schema(implementation = User.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/user/{userId}")
    public ResponseEntity<ResponseUserDTO> updateUser(@RequestBody @Valid UserDTO userDTO , @PathVariable UUID userId , @RequestHeader("Authorization") String token) {
        ResponseUserDTO updatedUser = userService.updateUser(userId, userDTO , token ,"PUT");
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        if(validationService.validateRole(token , "PUT") && !validationService.isTokenExpired(token)){
//        }
//            ResponseUserDTO updatedUser = userService.updateUser(userId, newUserData);
//            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//        }
//        else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }

    }

    @Operation(
            summary = "Delete a User By Id",
            description = "Delete a User Object by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID userId , @RequestHeader("Authorization") String token) {
        userService.deleteUserById(userId , token , "DELETE");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//        if(validationService.validateRole(token , "DELETE") && !validationService.isTokenExpired(token)){
//            userService.deleteUserById(userId);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        else{
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }

    }





}
