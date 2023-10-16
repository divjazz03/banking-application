package com.example.bankingapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;

    private String lastName;

    private String middleName;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String address;

    private String stateOfOrigin;

    private String accountNumber;

    private BigDecimal accountBalance;

    private String Email;

    private String phoneNumber;

    private String alternativePhoneNumber;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
