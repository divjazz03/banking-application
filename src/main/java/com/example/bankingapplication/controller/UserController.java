package com.example.bankingapplication.controller;

import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.UserRequest;
import com.example.bankingapplication.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.createAccount(userRequest));
    }
}
