package com.example.bankingapplication.service;

import com.example.bankingapplication.dto.EmailDetails;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailAlert(EmailDetails details);
    void sendEmailWithAttachment(EmailDetails details) throws MessagingException;

}
