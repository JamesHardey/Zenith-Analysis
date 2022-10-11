package com.jcoding.zenithanalysis.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Service
public class ZenithEmailSenderServices {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String subject, String body, List<String> emails)
            throws MessagingException, UnsupportedEncodingException {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@zenith-analysis.com");
        message.setText(body);
        message.setSubject(subject);
        emails.stream()
                .forEach((email -> {
                    message.setTo(email);
                    mailSender.send(message);
                }));

    }

    public void sendMailWithAttachment(String subject, String body, List<String> emails, String attachment)
            throws MessagingException, UnsupportedEncodingException{

        MimeMessage mimeMessage = new MimeMessage(mailSender.createMimeMessage());
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("no-reply@zenith-analysis.com");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);
        FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
        mimeMessageHelper.addAttachment(
                Objects.requireNonNull(
                    fileSystemResource.getFilename()
                ),
                fileSystemResource
        );

        emails.stream()
            .forEach((email -> {
                try {
                    mimeMessageHelper.setTo(email);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                mailSender.send(mimeMessage);
        }));

    }

    public void sendVerificationEmail(String email, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = email;
        String fromAddress = "zenithsanalysis@gmail.com";
        String senderName = "Zenith-Analysis";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Your verification code for the registration is:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Zenith Analysis.";

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
