package com.sadang.storybada.hall;

import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.service.HallService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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
    public String hall_ranking(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        session.setAttribute("userDTO", newUserDTO);

        // pagination 관련
        Page<Hall> hallPage = hallService.getHallPage(page);
        List<HallDTO> hallDTOList = hallService.getHallDTOList(page);
        Map<String, Map> pagingData = hallService.getHallPageData(page);

        model.addAttribute("hallDTOList", hallDTOList);
        model.addAttribute("hallPage", hallPage);
        model.addAttribute("startPage", pagingData.get("integerMap").get("startPage"));
        model.addAttribute("endPage", pagingData.get("integerMap").get("endPage"));
        model.addAttribute("hasPrevGroup", pagingData.get("booleanMap").get("hasPrevGroup"));
        model.addAttribute("hasNextGroup", pagingData.get("booleanMap").get("hasNextGroup"));
        model.addAttribute("prevGroupPage", pagingData.get("integerMap").get("prevGroupPage"));
        model.addAttribute("nextGroupPage", pagingData.get("integerMap").get("nextGroupPage"));

        return "hall/ranking";
    }

    @GetMapping("/ranking/detail")
    public String hall_ranking_detail(HttpSession session, Model model, @RequestParam long id) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        HallDTO hallDTO = hallService.getHallDTOById(id);

        session.setAttribute("userDTO", newUserDTO);
        model.addAttribute("hallDTO", hallDTO);
        model.addAttribute("id", id);

        return "hall/ranking-detail";
    }
}
