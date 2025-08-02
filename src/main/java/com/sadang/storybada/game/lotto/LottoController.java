package com.sadang.storybada.game.lotto;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.lotto.service.LottoBufferService;
import com.sadang.storybada.game.lotto.service.LottoHallService;
import com.sadang.storybada.game.lotto.service.LottoHistoryService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class LottoController {

    private final LottoHallService lottoHallService;
    private final UserService userService;

    @GetMapping("/lotto")
    public String lotto_view(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("myLottoHistory", lottoHallService.getMyLottoHistory(nameId));

        return "game/lotto";
    }
}
