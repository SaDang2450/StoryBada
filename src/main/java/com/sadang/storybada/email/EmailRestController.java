package com.sadang.storybada.email;

import com.sadang.storybada.dto.EmailConfirmDTO;
import com.sadang.storybada.dto.EmailRequestDTO;
import com.sadang.storybada.email.service.EmailService;
import com.sadang.storybada.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailRestController {

    private final EmailService emailService;

    public EmailRestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ApiResponse<Boolean> sendJoinMail(@RequestParam String email) {

        emailService.joinEmail(email);

        return ApiResponse.success(true);
    }

    @PostMapping("/confirm")
    public ApiResponse<Boolean> mailConfirm(@RequestParam String email, @RequestParam String authNum) {

        Boolean result = emailService.verifyAuthNum(email, authNum);

        return ApiResponse.success(result);
    }


}
