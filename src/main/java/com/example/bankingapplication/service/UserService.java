package com.example.bankingapplication.service;


import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.CreditDebitRequest;
import com.example.bankingapplication.dto.EnquiryRequest;
import com.example.bankingapplication.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest request);

    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);
}
