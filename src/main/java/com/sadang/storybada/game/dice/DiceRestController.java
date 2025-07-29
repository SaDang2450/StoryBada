package com.sadang.storybada.game.dice;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.hp.service.HpService;
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
    private final HpService hpService;

    @PostMapping("bet")
    public ApiResponse<Boolean> addBetting(HttpSession session, @RequestParam String betting, @RequestParam long hp) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        if (diceBufferService.insertDiceBetting(userDTO, betting, hp) == null) {

            return ApiResponse.success(false);
        } else {

            userDTO = userDTO.toBuilder().point(hpService.getCurrentPointByNameId(userDTO.getMainNameId())).build();
            session.setAttribute("userDTO", userDTO);

            return ApiResponse.success(true);
        }
    }
}
