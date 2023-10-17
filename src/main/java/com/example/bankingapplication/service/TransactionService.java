package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.TransactionDTO;

public interface TransactionService {
    void saveTransaction(TransactionDTO transaction);

}
