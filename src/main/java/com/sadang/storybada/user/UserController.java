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

    @GetMapping("/mypage")
    public String mypage_view() {

        return "user/mypage";
    }

    @GetMapping("/myhall")
    public String myhall_view() {

        return "user/myhall";
    }

    @GetMapping("/change")
    public String change_view() {

        return "user/change";
    }
}
