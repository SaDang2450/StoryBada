package com.sadang.storybada.game;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
public class GameController {

//    @GetMapping("/dice")
//    public String dice_view() {
//
//        return "game/dice";
//    }
//
//    @GetMapping("/box")
//    public String box_view() {
//
//        return "game/box";
//    }

    @GetMapping("/lotto")
    public String lotto_view() {

        return "game/lotto";
    }
//
//    @GetMapping("/box/history")
//    public String box_history_view() {
//
//        return "game/box-history";
//    }
}

