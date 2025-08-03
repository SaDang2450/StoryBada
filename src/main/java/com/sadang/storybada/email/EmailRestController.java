package com.sadang.storybada.email;

import com.sadang.storybada.dto.EmailConfirmDTO;
import com.sadang.storybada.dto.EmailRequestDTO;
import com.sadang.storybada.email.service.EmailService;
import com.sadang.storybada.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
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
    public ApiResponse<Boolean> sendJoinMail(HttpSession session, @RequestParam String email) {

        int authNumber = emailService.joinEmail(email);
        session.setAttribute("authNumber", authNumber);

        return ApiResponse.success(true);
    }

    @PostMapping("/confirm")
    public ApiResponse<Boolean> mailConfirm(HttpSession session, @RequestParam int authNum) {

        int authNumber = (int) session.getAttribute("authNumber");
        Boolean result = emailService.verifyAuthNum(authNum, authNumber);

        return ApiResponse.success(result);
    }


}
