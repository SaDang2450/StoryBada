package com.sadang.storybada.hall;

import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.PaginationDTO;
import com.sadang.storybada.dto.LeftHallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.service.HallService;
import com.sadang.storybada.hall.service.LeftHallService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/hall")
@RequiredArgsConstructor
public class HallController {

    private final UserService userService;
    private final HallService hallService;
    private final LeftHallService leftHallService;

    @GetMapping("/register")
    public String hallRegister(HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());

        session.setAttribute("userDTO", newUserDTO);

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "hall/register";
    }

    @GetMapping("/ranking")
    public String hallRanking(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        if (userDTO != null) {
            UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
            session.setAttribute("userDTO", newUserDTO);
        }

        // pagination 관련
        Page<Hall> hallPage = hallService.getHallPage(page);
        List<HallDTO> hallDTOList = hallService.getHallDTOList(page);
        PaginationDTO hallPageDTO = hallService.makeHallPageDTO(page);

        model.addAttribute("hallPage", hallPage);
        model.addAttribute("hallDTOList", hallDTOList);
        model.addAttribute("hallPageDTO", hallPageDTO);

        //
        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "hall/ranking";
    }

    @GetMapping("/ranking/detail")
    public String hallRankingDetail(HttpSession session, Model model, @RequestParam long id) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        if (userDTO != null) {
            UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
            session.setAttribute("userDTO", newUserDTO);
        }

        HallDTO hallDTO = hallService.getHallDTOById(id);

        model.addAttribute("hallDTO", hallDTO);
        model.addAttribute("id", id);

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "hall/ranking-detail";
    }
}
