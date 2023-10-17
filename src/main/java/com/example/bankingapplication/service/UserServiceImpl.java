package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.*;
import com.example.bankingapplication.model.Transaction;
import com.example.bankingapplication.model.enums.Status;
import com.example.bankingapplication.model.User;
import com.example.bankingapplication.model.enums.TransactionStatus;
import com.example.bankingapplication.model.enums.TransactionType;
import com.example.bankingapplication.repository.UserRepository;
import com.example.bankingapplication.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private EmailService emailService;

    private TransactionService transactionService;

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
        sendEmailAlert(savedUser);


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

    /**
     * Returns the Accounts Username
     * @param request
     * @return Account UserName (String)
     */
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
     * @param request Request DTO  that contains Credit/Debit info
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

        //Save Transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(userCredited.getAccountNumber())
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getAmount())
                .transactionType(TransactionType.CREDIT)
                .build();

        Transaction trans_info = transactionService.saveTransaction(transactionDTO);
        sendCreditAlert(userCredited, request.getAmount(), trans_info);


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
     * @param request A request DTO that contains credit / debit info
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

        //Save Transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(userDebited.getAccountNumber())
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getAmount())
                .transactionType(TransactionType.DEBIT)
                .build();

        Transaction trans_info = transactionService.saveTransaction(transactionDTO);
        sendDebitAlert(userDebited, request.getAmount(), trans_info);
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
     * @param request a transfer DTO for transfer info
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

        if ((senderUser.getAccountBalance().signum()) <= 0) {
            bankResponses.add(BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_BALANCE_IS_INSUFFICIENT_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_BALANCE_IS_INSUFFICIENT_MESSAGE)
                    .accountInfo(
                            AccountInfo.builder()
                                    .accountName(senderUser.getFirstName() + " " + senderUser.getLastName())
                                    .accountBalance(senderUser.getAccountBalance().toPlainString())
                                    .accountNumber(senderUser.getAccountNumber())
                                    .build()
                    )
                    .build());
            return bankResponses;
        }


        User sentUser = userRepository.save(senderUser);
        User receivedUser = userRepository.save(receiverUser);
        //Save Transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(sentUser.getAccountNumber())
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .build();

        Transaction trans_info = transactionService.saveTransaction(transactionDTO);

        transferAlert(sentUser, receivedUser, request.getAmount(), trans_info);

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
     * @param email Email to check for
     * @return true if user exists
     */
    public boolean checkUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    /**
     * Checks if a user with given account number exists in the Database
     *
     * @param accountNo Account number to check for
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

    /**
     * Sends an Email to the User after he must have been debited
     * @param debitedUser Object of debited User
     * @param amount Amount from request that was debited
     * @param transaction Transaction Object
     */
    public void sendDebitAlert(User debitedUser, BigDecimal amount, Transaction transaction){
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(debitedUser.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("Good day " + debitedUser.getFirstName() + ":\n" +
                        "The Sum of #" + amount +  " has been debited from your account \n" +
                        "Trans ID: " + transaction.getTransactionId())
                .build();
        emailService.sendEmailAlert(emailDetails);
    }

    /**
     * Sends an Email to the User after he must have been debited
     * @param creditedUser Object of debited User
     * @param amount Amount from request that was credited
     * @param transaction Transaction Object
     */
    public void sendCreditAlert(User creditedUser, BigDecimal amount, Transaction transaction){
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(creditedUser.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("Good day " + creditedUser.getFirstName() + ":\n" +
                        "The Sum of #" + amount +  " has been credited to your account \n" +
                        "Trans ID: " + transaction.getTransactionId())
                .build();
        emailService.sendEmailAlert(emailDetails);
    }

    /**
     * Sends an Email to the User after he must have been debited after a transfer operation
     * @param senderUser Object of debited User
     * @param receiverUser Object of receiver User
     * @param amount Amount from request that was debited
     * @param transaction Transaction Object
     */
    public void transferAlert(User senderUser, User receiverUser, BigDecimal amount, Transaction transaction){
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(senderUser.getEmail())
                .subject("Transfer Successful")
                .messageBody("You have successfully transferred #" + amount + " to " +
                        "" + receiverUser.getFirstName() + " " + receiverUser.getLastName() +"\n" +
                        " Trans ID: " + transaction.getTransactionId())
                .build();
        emailService.sendEmailAlert(emailDetails);
    }

}
