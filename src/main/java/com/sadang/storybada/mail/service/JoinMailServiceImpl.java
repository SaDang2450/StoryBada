package com.sadang.storybada.mail.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinMailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private String code;

    @Override
    public MimeMessage CreateMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("회원가입 인증번호");

        String msgg = "" +
                "<div>" +
                "   <h1>회원가입 인증 코드 <h1> <br>" +
                "   <span>" + code + "</span>" +
                "</div>";

        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("네이버 메일주소", "보내는 사용자이름"));

        return message;
    }

    @Override
    public String sendSimpleMessage(String to) throws Exception {
        code = createKey();
        MimeMessage message = CreateMessage(to);
        try {
            mailSender.send((MimeMessagePreparator) message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }

        return code;
    }

    @Override
    public String createKey() {
        Random random = new Random();
        int key = 100000 + random.nextInt(900000);

        return String.valueOf(key);
    }
}
