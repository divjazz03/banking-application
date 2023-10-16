package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails details);

}
