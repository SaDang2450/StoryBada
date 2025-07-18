package com.sadang.storybada.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/register")
    public String login_view() {

        return "user/register";
    }

    @GetMapping("/find")
    public String find_view() {
        return "user/find";
    }
}
