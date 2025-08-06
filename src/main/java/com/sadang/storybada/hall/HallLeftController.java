package com.sadang.storybada.hall;

import com.sadang.storybada.dto.LeftHallDTO;
import com.sadang.storybada.hall.service.LeftHallService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/hall/left")
@RequiredArgsConstructor
public class HallLeftController {

    private final LeftHallService leftHallService;

    @GetMapping("/daily")
    public String hallDaily(HttpSession session, Model model) {

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "fragments/left-side-zone :: hall";
    }

    @GetMapping("/weekly")
    public String hallWeekly(HttpSession session, Model model) {

        LocalDateTime[] range = leftHallService.getWeeklyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getWeeklyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "fragments/left-side-zone :: hall";
    }

    @GetMapping("/monthly")
    public String hallMonthly(HttpSession session, Model model) {

        LocalDateTime[] range = leftHallService.getMonthlyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getMonthlyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "fragments/left-side-zone :: hall";
    }
}
