package com.sadang.storybada.mail.service;

import jakarta.mail.internet.MimeMessage;

public interface MailService {

    MimeMessage CreateMessage(String to) throws Exception;

    String sendSimpleMessage(String to) throws Exception;

    public String createKey();
}
