package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.*;
import com.example.bankingapplication.model.Status;
import com.example.bankingapplication.model.User;
import com.example.bankingapplication.repository.UserRepository;
import com.example.bankingapplication.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private EmailService emailService;

    /**
     * This method saves a new User into the database
     *
     * @param request
     * @return returns BankResponse
     */
    @Override
    public BankResponse createAccount(UserRequest request) {
        if (checkUserExistByEmail(request.getEmail())) {
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
        // sendEmailAlert(savedUser); Not working yet


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance().toPlainString())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getMiddleName() + " " + savedUser.getLastName())
                        .build())
                .build();

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        if (!checkUserExistsByAccount(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(String.valueOf(user.getAccountBalance()))
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        if (!checkUserExistsByAccount(request.getAccountNumber())) {
            return AccountUtils.ACCOUNT_DOES_NOT_EXIST;
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());

        return "User's Name Result : " + foundUser.getFirstName() + " " + foundUser.getMiddleName() + " " + foundUser.getLastName();
    }

    /**
     * Credits destination account number if it exists
     *
     * @param request
     * @return BankResponse
     */
    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        //check if it exists
        if (!checkUserExistsByAccount(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        User userCredited = userRepository.save(userToCredit);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE + request.getAmount().toPlainString())
                .accountInfo(
                        AccountInfo.builder()
                                .accountNumber(userCredited.getAccountNumber())
                                .accountName(userCredited.getFirstName() + " " + userCredited.getLastName())
                                .accountBalance(userCredited.getAccountBalance().toPlainString())
                                .build()
                )
                .build();
    }

    /**
     * Debits destination account number if it exists
     *
     * @param request
     * @return BankResponse
     */
    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        if (!checkUserExistsByAccount(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                    .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        if (userToDebit.getAccountBalance().signum() < 0) {
            userRepository.save(userToDebit);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_BALANCE_IS_INSUFFICIENT_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_BALANCE_IS_INSUFFICIENT_MESSAGE)
                    .accountInfo(
                            AccountInfo.builder()
                                    .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                                    .accountBalance(userToDebit.getAccountBalance().toPlainString())
                                    .accountNumber(userToDebit.getAccountNumber())
                                    .build()
                    )
                    .build();
        }
        User userDebited = userRepository.save(userToDebit);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_CODE)
                .responseMessage(request.getAmount().toPlainString() + AccountUtils.ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE)
                .accountInfo(
                        AccountInfo.builder()
                                .accountNumber(userDebited.getAccountNumber())
                                .accountName(userDebited.getFirstName() + " " + userDebited.getLastName())
                                .accountBalance(userDebited.getAccountBalance().toPlainString())
                                .build()
                )
                .build();
    }

    /**
     * transfers an amount from one account specified in the request
     * to another
     *
     * @param request
     * @return BankResponse containing transfer result
     */
    @Override
    public List<BankResponse> transferToAccount(TransferRequest request) {

        List<BankResponse> bankResponses = new ArrayList<>();
        if (!(checkUserExistsByAccount(request.getAccountNoSender()) && checkUserExistsByAccount(request.getAccountNoRecipient()))) {
            if (!(checkUserExistsByAccount(request.getAccountNoSender()))) {
                bankResponses.add(
                        BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_OF_SENDER_DOES_NOT_EXIST_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_OF_SENDER_DOES_NOT_EXIST_MESSAGE)
                                .accountInfo(null)
                                .build()
                );
                return bankResponses;
            } else if (!(checkUserExistsByAccount(request.getAccountNoRecipient()))) {
                bankResponses.add(
                        BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_OF_RECIPIENT_DOES_NOT_EXIST_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_OF_RECIPIENT_DOES_NOT_EXIST_MESSAGE)
                                .accountInfo(null)
                                .build()
                );
                return bankResponses;
            } else {
                bankResponses.add(
                        BankResponse.builder()
                                .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXIST)
                                .responseCode(AccountUtils.ACCOUNT_DOES_NOT_EXIST_CODE)
                                .accountInfo(null)
                                .build()
                );
                return bankResponses;
            }
        }

        User senderUser = userRepository.findByAccountNumber(request.getAccountNoSender());
        User receiverUser = userRepository.findByAccountNumber(request.getAccountNoRecipient());

        senderUser.setAccountBalance(senderUser.getAccountBalance().subtract(request.getAmount()));
        receiverUser.setAccountBalance(receiverUser.getAccountBalance().add(request.getAmount()));

        User sentUser = userRepository.save(senderUser);
        User receivedUser = userRepository.save(receiverUser);

        bankResponses.add(
                BankResponse.builder()
                        .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                        .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE + ": You sent #" + request.getAmount() + " to " + receivedUser.getFirstName())
                        .accountInfo(
                                AccountInfo.builder()
                                        .accountName(sentUser.getFirstName() + " " + sentUser.getLastName())
                                        .accountNumber(sentUser.getAccountNumber())
                                        .accountBalance(sentUser.getAccountBalance().toPlainString())
                                        .build()
                        )
                        .build());
        bankResponses.add(
                BankResponse.builder()
                        .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                        .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE + ": You received #" + request.getAmount() + " from " + sentUser.getFirstName())
                        .accountInfo(
                                AccountInfo.builder()
                                        .accountName(receivedUser.getFirstName() + " " + receivedUser.getLastName())
                                        .accountNumber(receivedUser.getAccountNumber())
                                        .accountBalance(receivedUser.getAccountBalance().toPlainString())
                                        .build()
                        )
                        .build()
        );

        return bankResponses;
    }

    /**
     * Checks if a user with given email exists in the Database
     *
     * @param email
     * @return true if user exists
     */
    public boolean checkUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    /**
     * Checks if a user with given account number exists in the Database
     *
     * @param accountNo
     * @return true if user exists
     */
    public boolean checkUserExistsByAccount(String accountNo) {
        return userRepository.existsByAccountNumber(accountNo);
    }

    public void sendEmailAlert(User saveduser) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveduser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations, your account has been successfully created.\n" +
                        "Your Account Details:\n" +
                        "Account Name: " + saveduser.getFirstName() + " " + saveduser.getMiddleName() + " " + saveduser.getLastName() + "\n" +
                        "Account Number: " + saveduser.getAccountNumber() + "\n"
                )
                .build();
        emailService.sendEmailAlert(emailDetails);
    }

}
