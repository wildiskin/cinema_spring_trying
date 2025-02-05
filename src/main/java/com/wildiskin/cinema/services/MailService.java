package com.wildiskin.cinema.services;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender sender;

    @Autowired
    public MailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void sendMessage(String recipient, String msg) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        Address address = new InternetAddress(recipient);
        mimeMessage.addRecipient(MimeMessage.RecipientType.TO, address);
        mimeMessage.setText(msg, "utf-8");
        sender.send(mimeMessage);
    }
}
