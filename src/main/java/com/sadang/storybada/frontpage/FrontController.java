package com.sadang.storybada.frontpage;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.user.domain.User;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FrontController {

    private final UserService userService;

    @GetMapping("/frontpage")
    public String frontpage_view(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        if (userDTO != null) {
            UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
            session.setAttribute("userDTO", newUserDTO);
        }

        return "frontpage/frontpage";
    }

    @GetMapping("/frontpage/information")
    public String frontpage_information() {

        return "frontpage/information";
    }
}
