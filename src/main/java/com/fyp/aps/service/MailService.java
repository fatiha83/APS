package com.fyp.aps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    // public void sendResetEmail(String email, String resetLink) {
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setTo(email);
    //     message.setSubject("Password Reset Request");
    //     message.setText("Click the link to reset your password: " + resetLink);
    //     mailSender.send(message);
    // }

    public void sendResetEmail(String email, String resetLink) {
    try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + resetLink);
        mailSender.send(message);
        System.out.println("Email sent successfully to: " + email);
    } catch (Exception e) {
        System.err.println("Failed to send email to " + email);
        e.printStackTrace();  // this will show the exact reason in your console
        throw e;
    }
}

}
