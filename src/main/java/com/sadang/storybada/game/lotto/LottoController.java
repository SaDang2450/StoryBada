package com.sadang.storybada.game.lotto;

import com.sadang.storybada.dto.*;
import com.sadang.storybada.game.lotto.service.LottoBufferService;
import com.sadang.storybada.game.lotto.service.LottoHallService;
import com.sadang.storybada.game.lotto.service.LottoHistoryService;
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
public class LottoController {

    private final LottoHallService lottoHallService;
    private final LottoHistoryService lottoHistoryService;
    private final UserService userService;
    private final HpService hpService;
    private final LeftHallService leftHallService;

    @GetMapping("/lotto")
    public String lotto_view(HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        List<HallDTO> lottoHallDTOList = hpService.getTop10LottoHallDTO();
        List<LottoHistoryDTO> lottoHistoryDTOLIst = lottoHistoryService.getTop10RecentHistoryDTO();

        session.setAttribute("userDTO", newUserDTO);
        model.addAttribute("myLottoHistory", lottoHallService.getMyLottoHistory(nameId));
        model.addAttribute("lottoHallDTOList", lottoHallDTOList);
        model.addAttribute("lottoRecentDTOList", lottoHistoryDTOLIst);

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "game/lotto";
    }

    @GetMapping("/lotto/reload/recent")
    public String lotto_reload_recent(HttpSession session, Model model) {

        List<LottoHistoryDTO> lottoHistoryDTOList = lottoHistoryService.getTop10RecentHistoryDTO();

        model.addAttribute("lottoRecentDTOList", lottoHistoryDTOList);

        return "game/lotto :: tableLottoRecentFragment";
    }

    @GetMapping("/lotto/reload/hall")
    public String lotto_reload_hall(HttpSession session, Model model) {

        List<HallDTO> lottoHallDTOList = hpService.getTop10LottoHallDTO();

        model.addAttribute("lottoHallDTOList", lottoHallDTOList);

        return "game/lotto :: tableLottoHallFragment";
    }
}
