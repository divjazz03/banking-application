package com.example.bankingapplication.dto;

import com.example.bankingapplication.model.Gender;
import com.example.bankingapplication.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String firstName;

    private String lastName;

    private String middleName;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String address;

    private String stateOfOrigin;

    private String email;

    private String phoneNumber;

    private String alternativePhoneNumber;
}
