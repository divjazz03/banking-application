package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.AccountInfo;
import com.example.bankingapplication.dto.BankResponse;
import com.example.bankingapplication.dto.UserRequest;
import com.example.bankingapplication.model.Status;
import com.example.bankingapplication.model.User;
import com.example.bankingapplication.repository.UserRepository;
import com.example.bankingapplication.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    /**
     * This method saves a new User into the database
     * @param request
     * @return returns BankResponse
     */
    @Override
    public BankResponse createAccount(UserRequest request) {
        if (checkUserExistByEmail(request.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .gender(request.getGender())
                .address(request.getAddress())
                .stateOfOrigin(request.getStateOfOrigin())
                .phoneNumber(request.getPhoneNumber())
                .alternativePhoneNumber(request.getAlternativePhoneNumber())

                .email(request.getEmail())
                .status(Status.ACTIVE)
                .accountNumber(AccountUtils.generateAccountNo())
                .accountBalance(BigDecimal.ZERO)
                .build();
        User savedUser = userRepository.save(user);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance().toPlainString())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getMiddleName() + " " + savedUser.getLastName())
                        .build())
                .build();

    }

    public boolean checkUserExistByEmail(String email){
       return userRepository.existsByEmail(email);
    }

}
