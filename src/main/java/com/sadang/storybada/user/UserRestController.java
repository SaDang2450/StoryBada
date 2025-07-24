package com.sadang.storybada.user;

import com.sadang.storybada.dto.UserDTO;
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

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<Boolean> login(@RequestParam String loginId, @RequestParam String password, HttpSession session) {

        UserDTO userDTO = userService.getUser(loginId, password);

        if (userDTO == null) {
            return ApiResponse.fail(ResponseCode.USER_LOGIN_FAIL);
        } else {
            session.setAttribute("userId", userDTO.getId());
            session.setAttribute("mainName", userDTO.getMainName());

            return ApiResponse.success(true);
        }
    }

}
