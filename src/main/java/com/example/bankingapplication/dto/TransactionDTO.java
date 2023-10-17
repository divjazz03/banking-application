package com.example.bankingapplication.dto;

import com.example.bankingapplication.model.enums.TransactionStatus;
import com.example.bankingapplication.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private TransactionStatus transactionStatus;
}
