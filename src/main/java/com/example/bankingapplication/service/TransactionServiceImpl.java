package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.TransactionDTO;
import com.example.bankingapplication.model.Transaction;
import com.example.bankingapplication.model.enums.TransactionStatus;
import com.example.bankingapplication.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Data
@Service
public class TransactionServiceImpl implements TransactionService{

    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDTO.getTransactionType())
                .amount(transactionDTO.getAmount())
                .accountNumber(transactionDTO.getAccountNumber())
                .transactionStatus(TransactionStatus.SUCCESS)
                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }
}
