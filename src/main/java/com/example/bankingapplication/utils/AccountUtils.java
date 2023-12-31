package com.example.bankingapplication.utils;

import java.util.Random;
import java.time.Year;

/**
 * This class contains all utilities concerning the  <code><b>User</b></code> class
 */
public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";

    public static final String ACCOUNT_EXISTS_MESSAGE = "An account already exists";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";

    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Your account has been created successfully";

    public static final String ACCOUNT_DOES_NOT_EXIST_CODE = "003";

    public static final String ACCOUNT_DOES_NOT_EXIST = "Account does not exist. Try creating an Account";

    public static final String ACCOUNT_FOUND_SUCCESS_CODE = "004";

    public static final String ACCOUNT_FOUND_SUCCESS_MESSAGE = "Your Account was found";

    public static final String ACCOUNT_CREDITED_SUCCESSFULLY_CODE = "005";

    public static final String ACCOUNT_CREDITED_SUCCESSFULLY_MESSAGE = "Account successfully credited. Amount: ";

    public static final String ACCOUNT_DEBITED_SUCCESSFULLY_CODE = "006";

    public static final String ACCOUNT_DEBITED_SUCCESSFULLY_MESSAGE = " was debited from your account";

    public static final String ACCOUNT_OF_RECIPIENT_DOES_NOT_EXIST_CODE = "008";

    public static final String ACCOUNT_OF_RECIPIENT_DOES_NOT_EXIST_MESSAGE = "Account No of recipient does not exist";

    public static final String ACCOUNT_OF_SENDER_DOES_NOT_EXIST_CODE = "008";

    public static final String ACCOUNT_OF_SENDER_DOES_NOT_EXIST_MESSAGE = "Account No of recipient does not exist";

    public static final String ACCOUNT_BALANCE_IS_INSUFFICIENT_CODE = "007";
    public static final String ACCOUNT_BALANCE_IS_INSUFFICIENT_MESSAGE = "Your account balance is insufficient";

    public static final String TRANSFER_SUCCESSFUL_CODE = "009";

    public static final String TRANSFER_SUCCESSFUL_MESSAGE ="Transfer Successful";

    public static final String TRANSFER_FAILED_CODE = "010";

    public static final String TRANSFER_FAILED_MESSAGE = "Transfer failed";



    /**
     * Generates a random accountNumber with current Year
     * and random six digits
     * @return String
     */
    public static String generateAccountNo(){

        Random random = new Random();
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        int randNumber = random.nextInt(100000, 999999);
        return String.valueOf(currentYear) + String.valueOf(randNumber);
    }
}
