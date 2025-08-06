package com.sadang.storybada.frontpage;

import com.sadang.storybada.dto.LeftHallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.service.LeftHallService;
import com.sadang.storybada.user.domain.User;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FrontController {

    private final UserService userService;
    private final LeftHallService leftHallService;

    @GetMapping("/frontpage")
    public String frontpage_view(HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        if (userDTO != null) {
            UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
            session.setAttribute("userDTO", newUserDTO);
        }

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "frontpage/frontpage";
    }

    @GetMapping("/frontpage/information")
    public String frontpage_information(HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        if (userDTO != null) {
            UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
            session.setAttribute("userDTO", newUserDTO);
        }

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "frontpage/information";
    }
}
