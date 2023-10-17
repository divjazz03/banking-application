package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.TransactionDTO;
import com.example.bankingapplication.model.Transaction;

public interface TransactionService {
    Transaction  saveTransaction(TransactionDTO transaction);

}
