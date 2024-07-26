package com.example.bankingapp.controller;

import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.dto.UserDTO;
import com.example.bankingapp.entity.Transaction;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.service.TransactionService;
import com.example.bankingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        UserDTO createdUserDTO = userService.add(user).getBody();
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
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
    ResponseEntity<List<User>> getAllUsers(){
        return userService.returnAllUser();

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
    ResponseEntity<UserDTO> findUserById(@PathVariable UUID userId){
        return userService.findById(userId);
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
    ResponseEntity<UserDTO> updateUser(@RequestBody User newUserData , @PathVariable UUID userId){
        return userService.update(newUserData , userId);
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
    ResponseEntity<HttpStatus> deleteUSer(@PathVariable UUID userId){
        return  userService.delete(userId);
    }

    @Operation(
            summary = "Get all transactions of a User",
            description = "Retrieve all transactions of a User by their ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully", content = { @Content(schema = @Schema(implementation = Transaction.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "User not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })

    @GetMapping("/transactions/{userId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUserId(@PathVariable UUID userId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}
