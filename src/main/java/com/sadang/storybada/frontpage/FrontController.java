package com.sadang.storybada.frontpage;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/frontpage")
    public String frontpage_view(Model model, HttpSession session) {

        try {
            long userId = (long) session.getAttribute("userId");
            String mainName = (String) session.getAttribute("mainName");

            model.addAttribute("userId", userId);
            model.addAttribute("mainName", mainName);
        } catch (NullPointerException ignored) {

        }

        return "frontpage/frontpage";
    }

    @GetMapping("/frontpage/information")
    public String frontpage_information() {

        return "frontpage/information";
    }
}
