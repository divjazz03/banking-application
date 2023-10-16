package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.*;
import com.example.bankingapplication.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.createAccount(userRequest));
    }

    @GetMapping("/balanceEnquiry")
    public ResponseEntity<BankResponse> balanceEnquiry(@RequestBody EnquiryRequest request){
        return ResponseEntity.ok(userService.balanceEnquiry(request));
    }

    @GetMapping("/nameEnquiry")
    public ResponseEntity<String> nameEnquiry(@RequestBody EnquiryRequest request){
        return ResponseEntity.ok(userService.nameEnquiry(request));
    }

    @PostMapping("credit")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return ResponseEntity.ok(userService.creditAccount(creditDebitRequest));
    }

    @PostMapping("debit")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return ResponseEntity.ok(userService.debitAccount(creditDebitRequest));
    }

    @PostMapping("transfer")
    public ResponseEntity<List<BankResponse>> transferToAccount(@RequestBody TransferRequest transferRequest){
        return ResponseEntity.ok(userService.transferToAccount(transferRequest));

    }
}
