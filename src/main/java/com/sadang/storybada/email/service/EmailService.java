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

    private final RedisUtil redisUtil;
    private final JavaMailSender mailSender;


    @Value("${email.username}")
    private String setFrom;

    public EmailService(RedisUtil redisUtil, JavaMailSender mailSender) {
        this.redisUtil = redisUtil;
        this.mailSender = mailSender;
    }

    public int makeRandomNumber() {
        Random random = new Random();

        return Integer.parseInt(String.valueOf(random.nextInt(900000) + 100000));
    }


    public int joinEmail(String email) {
        int authNumber = makeRandomNumber();
        String title = "[이야기바다] 회원가입 인증 메일입니다";
        String content = "<div>" +
                "   <h1>회원가입 인증 번호</h1><br> " +
                "   <span>" + authNumber + "</span>" +
                "</div>";
        sendEmail(setFrom, email, title, content);
        return authNumber;


//        redisUtil.saveAuthNumber(Integer.toString(authNumber), email, EXPIRATION);
    }

    public void findEmail(String email, String tempPassword) {

        String title = "[이야기바다] 임시 비밀빈호 메일입니다";
        String content = "<div>" +
                "   <h1>임시 비밀번호입니다.</h1> <br>" +
                "   <span>" + tempPassword + "</span>" +
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
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public Boolean verifyAuthNum(int authNum, int authNumber) {

        return authNum == authNumber;
//        return redisUtil.getData(authNum).equals(email);
    }

}
