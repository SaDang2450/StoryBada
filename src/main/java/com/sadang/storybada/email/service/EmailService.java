package com.sadang.storybada.email.service;

import com.sadang.storybada.common.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private static final int EXPIRATION = 300000;
    private final RedisUtil redisUtil;
    private final JavaMailSender mailSender;
    private int authNumber;

    @Value("${email.username}")
    private String setFrom;

    public EmailService(RedisUtil redisUtil, JavaMailSender mailSender) {
        this.redisUtil = redisUtil;
        this.mailSender = mailSender;
    }

    public void makeRandomNumber() {
        Random random = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNumber.append(random.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber.toString());
    }

    public void joinEmail(String email) {
        makeRandomNumber();
        String title = "[이야기바다] 회원가입 인증 메일입니다";
        String content = "" +
                "<div>" +
                "   <h1>회원가입 인증 번호</h1><br> " +
                "   <span>" + authNumber + "</span>" +
                "</div>";
        sendEmail(setFrom, email, title, content);
    }

    public void sendEmail(String setFrom, String email, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(setFrom);
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(content);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        redisUtil.saveAuthNumber(Integer.toString(authNumber), email, EXPIRATION);
    }

    public Boolean verifyAuthNum(String email, String authNum) {

        return redisUtil.getData(authNum).equals(email);
    }
}
