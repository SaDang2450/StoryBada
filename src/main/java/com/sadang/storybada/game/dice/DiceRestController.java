package com.sadang.storybada.game.dice;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
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
    public ApiResponse<Void> addBetting(HttpSession session, @RequestParam String betting, @RequestParam long hp) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        // Validation
        ResponseCode responseCode = diceBufferService.diceBettingValidation(userDTO, betting, hp);
        if (responseCode != ResponseCode.SUCCESS) {

            return ApiResponse.fail(responseCode);
        }

        // betting
        diceBufferService.insertDiceBetting(userDTO, betting, hp);

        // Reload
        userDTO = userDTO.toBuilder().point(hpService.getCurrentPointByNameId(userDTO.getMainNameId())).build();
        session.setAttribute("userDTO", userDTO);

        return ApiResponse.success(null);
    }
}
