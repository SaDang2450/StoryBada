package com.sadang.storybada.game.box;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.box.service.BoxBufferService;
import com.sadang.storybada.game.box.service.BoxHallService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class BoxController {

    private final BoxBufferService boxBufferService;
    private final BoxHallService boxHallService;
    private final UserService userService;

    @GetMapping("/box")
    public String box_view(HttpSession session) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("holdingBoxAmount", boxBufferService.getHoldingBoxAmount(nameId));
        session.setAttribute("averageBoxGetPoint", boxHallService.getAverageGetPoint(nameId));

        return "game/box";
    }

    @GetMapping("/box/history")
    public String box_history_view() {

        return "game/box-history";
    }


}
