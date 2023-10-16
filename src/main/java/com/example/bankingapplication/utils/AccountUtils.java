package com.example.bankingapplication.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";

    public static final String ACCOUNT_EXISTS_MESSAGE = "An account already exists";

    public static final String ACCOUNT_CREATION_SUCCESS = "002";

    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Your account has been created successfully";

    /**
     * Generates a random accountNumber with current Year
     * and random six digits
     * @return String
     */
    public static String generateAccountNo(){
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        int randNumber =(int) (min + Math.floor((Math.random() * max)));
        return String.valueOf(currentYear) + String.valueOf(randNumber);
    }
}
