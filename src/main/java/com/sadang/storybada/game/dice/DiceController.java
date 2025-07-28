package com.sadang.storybada.game.dice;

import com.sadang.storybada.game.dice.service.DiceBufferService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class DiceController {

    private final DiceBufferService diceBufferService;

    @GetMapping("/dice")
    public String dice_view(HttpSession session) {

        long mainNameId = (Long) session.getAttribute("mainNameId");
        session.setAttribute("diceBettingTotal",diceBufferService.getTotalBettingAmount(mainNameId));

        return "game/dice";
    }
}
