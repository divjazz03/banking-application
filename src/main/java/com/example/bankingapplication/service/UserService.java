package com.example.bankingapplication.service;


import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.UserRequest;

public interface UserService {

    public BankResponse createAccount(UserRequest request);
}
