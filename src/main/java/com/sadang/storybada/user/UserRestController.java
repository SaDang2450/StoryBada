package com.sadang.storybada.user;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.email.service.EmailService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserRestController(UserService userService, EmailService emailService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestParam String loginId, @RequestParam String password, HttpSession session) {

        UserDTO userDTO = userService.getUser(loginId, password);

        if (userDTO == null) {
            return ApiResponse.fail(ResponseCode.USER_LOGIN_FAIL);
        } else {
            session.setAttribute("userId", userDTO.getId());
            session.setAttribute("mainName", userDTO.getMainName());

            return ApiResponse.success(null);
        }
    }

    @PostMapping("/create")
    public ApiResponse<Boolean> create(@RequestParam String loginId, @RequestParam String password, @RequestParam String name, @RequestParam String email) {

        return ApiResponse.success(userService.addUser(loginId, password, name, email));
    }

    @PostMapping("/duplicate-id")
    public ApiResponse<Boolean> duplicateId(@RequestParam String loginId) {

        return ApiResponse.success(userService.duplicateIdCheck(loginId));
    }

    @PostMapping("/duplicate-email")
    public ApiResponse<Boolean> duplicateEmail(@RequestParam String email) {

        return ApiResponse.success(userService.duplicateEmailCheck(email));
    }
}
