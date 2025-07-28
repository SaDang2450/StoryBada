package com.sadang.storybada.game.dice;

import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/dice")
@RequiredArgsConstructor
public class DiceRestController {

    private final DiceBufferService diceBufferService;

    @PostMapping("bet")
    public ApiResponse<Boolean> addBetting(HttpSession session, @RequestParam String betting, @RequestParam long hp) {

        long mainNameId = (Long) session.getAttribute("mainNameId");
        diceBufferService.insertDiceBetting(mainNameId, betting, hp);

        return ApiResponse.success(true);
    }
}
