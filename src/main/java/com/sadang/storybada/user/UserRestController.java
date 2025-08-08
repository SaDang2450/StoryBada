package com.sadang.storybada.user;

import com.sadang.storybada.common.LoginCounter;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final NameService nameService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestParam String loginId, @RequestParam String password, HttpSession session) {

        UserDTO userDTO = userService.getUser(loginId, password);

        if (userDTO == null) {
            return ApiResponse.fail(ResponseCode.USER_LOGIN_FAIL);
        }

        if (session.getAttribute("userDTO") == null) {
            LoginCounter.increment();
        }

        session.setAttribute("userDTO", userDTO);

        return ApiResponse.success(null);

    }

    @PostMapping("/create")
    public ApiResponse<Boolean> create(@RequestParam String loginId, @RequestParam String password, @RequestParam String name, @RequestParam String email) {

        if (userService.addUser(loginId, password, name, email) == null) {
            return ApiResponse.success(false);
        } else {
            return ApiResponse.success(true);
        }
    }

    @PostMapping("/duplicate-id")
    public ApiResponse<Boolean> duplicateId(@RequestParam String loginId) {

        return ApiResponse.success(userService.duplicateIdCheck(loginId));
    }

    @PostMapping("/duplicate-email")
    public ApiResponse<Boolean> duplicateEmail(@RequestParam String email) {

        return ApiResponse.success(userService.duplicateEmailCheck(email));
    }

    @PostMapping("/find-register")
    public ApiResponse<Boolean> findRegister(@RequestParam String loginId, @RequestParam String email) {

        if (userService.findPasswordByEmail(loginId, email)) {

            return ApiResponse.success(true);
        } else {

            return ApiResponse.success(false);
        }
    }

    @PutMapping("/update")
    public ApiResponse<Boolean> update(HttpSession session, @RequestParam String password) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        session.removeAttribute("userDTO");

        return ApiResponse.success(userService.updatePassword(userDTO, password));
    }

    @DeleteMapping("/unregister")
    public ApiResponse<ResponseCode> passwordConfirm(HttpSession session, @RequestParam String password) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        // 비밀번호 확인 후 삭제
        if (userService.passwordConfirm(userDTO, password)) {
            userService.deleteUser(userDTO);
            return ApiResponse.success(ResponseCode.SUCCESS);
        } else {
            return ApiResponse.success(ResponseCode.USER_PASSWORD_INCORRECT);
        }
    }

}