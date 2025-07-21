package com.Brew_Track.Cafe.Brew_Track.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> ccList) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("brewtrack00@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (ccList != null && !ccList.isEmpty()) {
            message.setCc(getCcArray(ccList));
        }
        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList) {
        String[] ccArray = new String[ccList.size()];
        for (int i = 0; i < ccList.size(); i++) {
            ccArray[i] = ccList.get(i);
        }
        return ccArray;
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException{
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("brewtrack00@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your login details for Brew Track System:</b> " + to +
        "<br><b>Password:</b> " + password +
        "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg, "text/html");
        emailSender.send(message);
}

}