package com.sadang.storybada.frontpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/frontpage")
    public String frontpage_view() {

        return "frontpage/frontpage";
    }

    @GetMapping("/frontpage/information")
    public String frontpage_information() {

        return "frontpage/information";
    }
}
