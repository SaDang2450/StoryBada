package com.sadang.storybada.hall;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hall")
public class HallController {

    @GetMapping("/register")
    public String hall_register() {

        return "hall/register";
    }

    @GetMapping("/ranking")
    public String hall_ranking() {

        return "hall/ranking";
    }

    @GetMapping("/ranking/detail")
    public String hall_ranking_detail(@RequestParam int id, Model model) {

        model.addAttribute("id", id);

        return "hall/ranking-detail";
    }
}
