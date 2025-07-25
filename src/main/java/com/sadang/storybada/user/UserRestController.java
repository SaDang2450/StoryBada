package com.sadang.storybada.user;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.mail.service.JoinMailServiceImpl;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    private final JoinMailServiceImpl joinMailServiceImpl;

    public UserRestController(UserService userService, JoinMailServiceImpl joinMailServiceImpl) {
        this.userService = userService;
        this.joinMailServiceImpl = joinMailServiceImpl;
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
    public ApiResponse<Void> create(@RequestParam String loginId, @RequestParam String password, @RequestParam String name, @RequestParam String email, @RequestParam String sign) {

        return null;
    }

    @PostMapping("/duplicate-id")
    public ApiResponse<Boolean> duplicateId(@RequestParam String loginId) {

        return ApiResponse.success(userService.duplicateIdCheck(loginId));
    }

    @PostMapping("/email-request")
    public ApiResponse<Boolean> emailRequest(@RequestParam String email) throws Exception {

        if (userService.duplicateEmailCheck(email)) {
            joinMailServiceImpl.sendSimpleMessage(email);
            return ApiResponse.success(true);
        } else {
            return ApiResponse.success(false);
        }
    }
}
