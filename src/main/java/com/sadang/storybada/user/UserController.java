package com.sadang.storybada.user;

import com.sadang.storybada.common.LoginCounter;
import com.sadang.storybada.dto.PaginationDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.service.HallService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final HallService hallService;

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
    public String myhall_view(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        session.setAttribute("userDTO", newUserDTO);

        PaginationDTO myHallPaginationDTO = hallService.getMyHallPaginationDTO(page, userDTO.getId());
        model.addAttribute("myHallPaginationDTO", myHallPaginationDTO);

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

        if(session.getAttribute("userDTO") != null) {
            LoginCounter.decrement();
        }

        session.invalidate();

        return "redirect:/frontpage";
    }
}
