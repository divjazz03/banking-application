package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.*;
import com.example.bankingapplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Tag(name = "User Account Management APIs")
public class UserController {

    UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating a new User and assigning an Account Id"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.createAccount(userRequest));
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Queries for the account balance of the User"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest request){
        return ResponseEntity.ok(userService.balanceEnquiry(request));
    }

    @Operation(
            summary = "Name Enquiry",
            description = "Queries for the full name of the User"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/nameEnquiry")
    public ResponseEntity<String> nameEnquiry(@RequestBody EnquiryRequest request){
        return ResponseEntity.ok(userService.nameEnquiry(request));
    }

    @Operation(
            summary = "Credit Account",
            description = "Credits the User's Account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @PostMapping("credit")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return ResponseEntity.ok(userService.creditAccount(creditDebitRequest));
    }

    @Operation(
            summary = "Debit Account",
            description = "Debits the User's Account"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @PostMapping("debit")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return ResponseEntity.ok(userService.debitAccount(creditDebitRequest));
    }

    @Operation(
            summary = "Transfer Money",
            description = "Transfers certain amount from sender to receiver"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @PostMapping("transfer")
    public ResponseEntity<List<BankResponse>> transferToAccount(@RequestBody TransferRequest transferRequest){
        return ResponseEntity.ok(userService.transferToAccount(transferRequest));

    }
}
