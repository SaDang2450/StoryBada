package com.sadang.storybada.game.dice;

import com.sadang.storybada.dto.DiceHistoryDTO;
import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.LeftHallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.dice.service.DiceBufferService;
import com.sadang.storybada.game.dice.service.DiceHistoryService;
import com.sadang.storybada.hall.service.LeftHallService;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class DiceController {

    private final DiceBufferService diceBufferService;
    private final DiceHistoryService diceHistoryService;
    private final HpService hpService;
    private final UserService userService;
    private final LeftHallService leftHallService;

    @GetMapping("/dice")
    public String dice_view(HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long mainNameId = newUserDTO.getMainNameId();

        List<HallDTO> diceHallDTOList = hpService.getTop10DiceHallDTO();
        List<DiceHistoryDTO> diceHistoryDTOList = diceHistoryService.getTop10RecentHistoryDTO();

        session.setAttribute("userDTO", newUserDTO);
        model.addAttribute("diceBettingTotal",diceBufferService.getTotalBettingAmount(mainNameId));
        model.addAttribute("diceHallDTOList", diceHallDTOList);
        model.addAttribute("diceRecentDTOList", diceHistoryDTOList);

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "game/dice";
    }

    @GetMapping("/dice/reload/recent")
    public String dice_reload_recent(HttpSession session, Model model) {

        List<DiceHistoryDTO> diceHistoryDTOList = diceHistoryService.getTop10RecentHistoryDTO();

        model.addAttribute("diceRecentDTOList", diceHistoryDTOList);

        return "game/dice :: tableDiceRecentFragment";
    }

    @GetMapping("/dice/reload/hall")
    public String dice_reload_hall(HttpSession session, Model model) {

        List<HallDTO> diceHallDTOList = hpService.getTop10DiceHallDTO();

        model.addAttribute("diceHallDTOList", diceHallDTOList);

        return "game/dice :: tableDiceHallFragment";
    }

}
