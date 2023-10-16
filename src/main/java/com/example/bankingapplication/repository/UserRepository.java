package com.example.bankingapplication.repository;

import com.example.bankingapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
