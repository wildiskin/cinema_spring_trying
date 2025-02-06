package com.wildiskin.cinema.services;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Component
public class MailService {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String senderAddress;

//    @Value("${mail.sender.password}")
//    private String senderPwd;


    public void sendMessage(String recipient, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(senderAddress);
        msg.setTo(recipient);
        msg.setSubject("testing!");
        msg.setText(text);
        sender.send(msg);
    }
}
