package com.sadang.storybada.game.box;

import com.sadang.storybada.dto.*;
import com.sadang.storybada.game.box.domain.BoxHall;
import com.sadang.storybada.game.box.service.BoxBufferService;
import com.sadang.storybada.game.box.service.BoxHallService;
import com.sadang.storybada.game.box.service.BoxHistoryService;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final BoxHistoryService boxHistoryService;
    private final UserService userService;
    private final HpService hpService;

    @GetMapping("/box")
    public String box_view(HttpSession session, Model model) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        List<HallDTO> boxHallDTOList = hpService.getTop10BoxHallDTO();
        List<BoxHistoryDTO> boxHistoryDTOList = boxHistoryService.getTop10RecentHistoryDTO();

        session.setAttribute("userDTO", newUserDTO);
        model.addAttribute("holdingBoxAmount", boxBufferService.getHoldingBoxAmount(nameId));
        model.addAttribute("averageBoxGetPoint", boxHallService.getAverageGetPoint(nameId));
        model.addAttribute("boxHallDTOList", boxHallDTOList);
        model.addAttribute("boxRecentDTOList", boxHistoryDTOList);

        return "game/box";
    }

    @GetMapping("/box/reload/recent")
    public String box_reload_recent(HttpSession session, Model model) {

        List<BoxHistoryDTO> boxHistoryDTOList = boxHistoryService.getTop10RecentHistoryDTO();

        model.addAttribute("boxRecentDTOList", boxHistoryDTOList);

        return "game/box :: tableBoxRecentFragment";
    }

    @GetMapping("/box/reload/hall")
    public String box_reload_hall(HttpSession session, Model model) {

        List<HallDTO> boxHallDTOList = hpService.getTop10BoxHallDTO();

        model.addAttribute("boxHallDTOList", boxHallDTOList);

        return "game/box :: tableBoxHallFragment";
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
