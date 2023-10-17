package com.example.bankingapplication.repository;

import com.example.bankingapplication.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
