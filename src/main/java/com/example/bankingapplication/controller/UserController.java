package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.CreditDebitRequest;
import com.example.bankingapplication.dto.EnquiryRequest;
import com.example.bankingapplication.dto.UserRequest;
import com.example.bankingapplication.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
