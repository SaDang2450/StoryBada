package com.sadang.storybada.hall;

import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.service.HallService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/hall")
@RequiredArgsConstructor
public class HallController {

    private final UserService userService;
    private final HallService hallService;

    @GetMapping("/register")
    public String hall_register(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        session.setAttribute("userDTO", newUserDTO);

        return "hall/register";
    }

    @GetMapping("/ranking")
    public String hall_ranking(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        List<HallDTO> HallDTOList = hallService.getHallDTOList();

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("HallDTOList", HallDTOList);

        return "hall/ranking";
    }

    @GetMapping("/ranking/detail")
    public String hall_ranking_detail(HttpSession session, @RequestParam long id, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        HallDTO hallDTO = hallService.getHallDTOById(id);

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("hallDTO", hallDTO);
        model.addAttribute("id", id);

        return "hall/ranking-detail";
    }
}
