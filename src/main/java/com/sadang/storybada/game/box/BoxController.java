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
        int maxPageButtons = 10;
        Page<BoxHall> myBoxHistoryPage = boxHallService.getMyHistoryPage(nameId, page);
        List<MyBoxHistoryDTO> myBoxHistoryDTOList = boxHallService.getMyBoxHisotryDTOListPage(nameId, page);

        int currentPage = myBoxHistoryPage.getNumber();
        int totalPages = myBoxHistoryPage.getTotalPages();

        if (totalPages == 0) {
            totalPages = 1;
        }

        int currentGroup = currentPage / maxPageButtons;
        int startPage = currentGroup * maxPageButtons + 1;
        int endPage = Math.min(startPage + maxPageButtons - 1, totalPages);

        if (startPage > endPage) {
            endPage = startPage;
        }

        boolean hasPrevGroup = startPage > 1;
        boolean hasNextGroup = endPage < totalPages - 1;

        int prevGroupPage = Math.max(startPage - 1, 1);
        int nextGroupPage = (endPage + 1) >= totalPages ? totalPages - 1 : endPage + 1;

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("myBoxHistoryDTOList", myBoxHistoryDTOList);
        session.setAttribute("myBoxHistoryPage", myBoxHistoryPage);
        session.setAttribute("startPage", startPage);
        session.setAttribute("endPage", endPage);
        session.setAttribute("hasPrevGroup", hasPrevGroup);
        session.setAttribute("hasNextGroup", hasNextGroup);
        session.setAttribute("prevGroupPage", prevGroupPage);
        session.setAttribute("nextGroupPage", nextGroupPage);

        return "game/box-history";
    }
}
