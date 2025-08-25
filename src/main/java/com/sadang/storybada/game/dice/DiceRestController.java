package com.sadang.storybada.game.dice;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.game.dice.service.DiceHistoryService;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/game/dice")
@RequiredArgsConstructor
public class DiceRestController {

    private final DiceBufferService diceBufferService;
    private final DiceHistoryService diceHistoryService;
    private final UserService userService;
    private final HpService hpService;

    @PostMapping("/bet")
    public ApiResponse<Long> addBetting(HttpSession session, @RequestParam String betting, @RequestParam long hp) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        // Validation
        ResponseCode responseCode = diceBufferService.diceBettingValidation(userDTO, betting, hp);
        if (responseCode != ResponseCode.SUCCESS) {

            return ApiResponse.fail(responseCode);
        }
//
        // betting
        diceBufferService.addDiceBetting(userDTO, betting, hp);

        // get diceBettingTotal
        long mainNameId = userDTO.getMainNameId();
        long diceBettingTotal = diceBufferService.getTotalBettingAmount(mainNameId);

        // Reload
//        userDTO = userDTO.toBuilder().point(hpService.getCurrentPointByNameId(userDTO.getMainNameId())).build();
//        session.setAttribute("userDTO", userDTO);

        return ApiResponse.success(diceBettingTotal);
    }

    @PostMapping("/reload")
    public Map<String, Object> reloadCurrentUserDTO(HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();
        long diceBettingTotal = diceBufferService.getTotalBettingAmount(nameId);

        session.setAttribute("userDTO", newUserDTO);

        resultMap.put("userDTO", newUserDTO);
        resultMap.put("diceBettingTotal", diceBettingTotal);

        return resultMap;
    }

    @GetMapping("/roll")
    public ApiResponse<Integer> diceRoll() {
        boolean recentResult = diceHistoryService.getVeryRecentResult();

        Random random = new Random();
        int randomNumber = random.nextInt(3);
        if (recentResult) {
            // 홀수
            switch (randomNumber) {
                case 0:
                    return ApiResponse.success(1);
                case 1:
                    return ApiResponse.success(3);
                case 2:
                    return ApiResponse.success(5);
            }
        } else {
            // 짝수
            switch (randomNumber) {
                case 0:
                    return ApiResponse.success(2);
                case 1:
                    return ApiResponse.success(4);
                case 2:
                    return ApiResponse.success(6);
            }
        }
        return ApiResponse.fail(ResponseCode.SUCCESS);
    }
}
