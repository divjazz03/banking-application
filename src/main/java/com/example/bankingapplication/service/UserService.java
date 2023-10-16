package com.example.bankingapplication.service;


import com.example.bankingapplication.dto.*;

import java.util.List;

public interface UserService {

    BankResponse createAccount(UserRequest request);

    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);

    List<BankResponse> transferToAccount(TransferRequest request);
}
