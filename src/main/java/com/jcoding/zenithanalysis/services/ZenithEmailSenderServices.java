package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class ZenithEmailSenderServices {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String subject, String body, String... toEmail)
            throws MessagingException, UnsupportedEncodingException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@zenith-analysis.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

    }

    public void sendVerificationEmail(String email, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", "James");
        String verifyURL = siteURL + "/verify?code=" + 12345;

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }



}
