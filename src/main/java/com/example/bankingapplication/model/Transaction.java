package com.example.bankingapplication.model;

import com.example.bankingapplication.model.enums.TransactionStatus;
import com.example.bankingapplication.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions" )
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;


}
