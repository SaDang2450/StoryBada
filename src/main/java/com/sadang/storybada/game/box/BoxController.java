package com.sadang.storybada.game.box;

import com.sadang.storybada.dto.MyBoxHistoryDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.box.domain.BoxHall;
import com.sadang.storybada.game.box.service.BoxBufferService;
import com.sadang.storybada.game.box.service.BoxHallService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
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
    public String box_history_view(HttpSession session, @RequestParam(defaultValue = "1") int page) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        // pagination 관련
        Page<BoxHall> myBoxHistoryPage = boxHallService.getMyHistoryPage(nameId, page);
        List<MyBoxHistoryDTO> myBoxHistoryDTOList = boxHallService.getMyBoxHisotryDTOListPage(nameId, page);
        Map<String, Map> pagingData = boxHallService.getMyHistoryPageData(nameId, page);

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("myBoxHistoryDTOList", myBoxHistoryDTOList);
        session.setAttribute("myBoxHistoryPage", myBoxHistoryPage);
        session.setAttribute("startPage", pagingData.get("integerMap").get("startPage"));
        session.setAttribute("endPage", pagingData.get("integerMap").get("endPage"));
        session.setAttribute("hasPrevGroup", pagingData.get("booleanMap").get("hasPrevGroup"));
        session.setAttribute("hasNextGroup", pagingData.get("booleanMap").get("hasNextGroup"));
        session.setAttribute("prevGroupPage", pagingData.get("integerMap").get("prevGroupPage"));
        session.setAttribute("nextGroupPage", pagingData.get("integerMap").get("nextGroupPage"));

        return "game/box-history";
    }
}
