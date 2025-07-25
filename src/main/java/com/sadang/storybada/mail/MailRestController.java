package com.sadang.storybada.mail;

import com.sadang.storybada.mail.service.JoinMailServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class MailRestController {

    private final JoinMailServiceImpl joinMailServiceImpl;

    public MailRestController(JoinMailServiceImpl joinMailServiceImpl) {
        this.joinMailServiceImpl = joinMailServiceImpl;
    }

    @PostMapping("/email-confirm")
    public String mailConfirm(@RequestParam("email") String email) throws Exception {

        return joinMailServiceImpl.sendSimpleMessage(email);
    }


}
