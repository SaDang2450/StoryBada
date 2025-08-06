package com.sadang.storybada.user;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String login_view() {

        return "user/register";
    }

    @GetMapping("/find")
    public String find_view(HttpSession session) {

        return "user/find";
    }

    @GetMapping("/mypage")
    public String mypage_view(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        session.setAttribute("userDTO", newUserDTO);
        return "user/mypage";
    }

    @GetMapping("/myhall")
    public String myhall_view(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        session.setAttribute("userDTO", newUserDTO);

        return "user/myhall";
    }

    @GetMapping("/change")
    public String change_view(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        session.setAttribute("userDTO", newUserDTO);

        return "user/change";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.removeAttribute("userDTO");

        return "redirect:/frontpage";
    }
}
