package com.example.bankingapp.controller;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "Account" , description = "Account Management Api ")
public class AccountController {
    private final AccountService accountService;

    @Operation(
            summary = " Account Information by User ID",
            description = "Retrieve all accounts associated with a User by their ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully", content = { @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "User not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/account/{userId}")
    List<AccountDTO> getaccountInfo(@PathVariable UUID userId) throws Exception {
        return accountService.getAccountInformation(userId);

    }
    @Operation(
            summary = "Create an Account for a User",
            description = "Create an Account for a User by User ID. The response is an AccountDTO containing account details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully", content = { @Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = { @Content(schema = @Schema()) })
    })

    @PostMapping("/account/{userId}")
    ResponseEntity<AccountDTO> createanaccount(@PathVariable UUID userId , @RequestBody AccountDTO accountDto) throws Exception {
        return accountService.createaccount(userId , accountDto);
    }
}

