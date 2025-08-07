package com.sadang.storybada.game.box;

import com.sadang.storybada.dto.*;
import com.sadang.storybada.game.box.domain.BoxHall;
import com.sadang.storybada.game.box.service.BoxBufferService;
import com.sadang.storybada.game.box.service.BoxHallService;
import com.sadang.storybada.game.box.service.BoxHistoryService;
import com.sadang.storybada.hall.service.LeftHallService;
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

import java.time.LocalDateTime;
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
    private final LeftHallService leftHallService;

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

        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

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
    public String box_history_view(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        session.setAttribute("userDTO", newUserDTO);

        // pagination 관련
        Page<BoxHall> myBoxHistoryPage = boxHallService.getMyHistoryPage(nameId, page);
        List<MyBoxHistoryDTO> myBoxHistoryDTOList = boxHallService.getMyBoxHisotryDTOListPage(nameId, page);
        PaginationDTO boxPageDTO = boxHallService.makeBoxHallPageDTO(nameId, page);

        model.addAttribute("myBoxHistoryDTOList", myBoxHistoryDTOList);
        model.addAttribute("myBoxHistoryPage", myBoxHistoryPage);
        model.addAttribute("boxPageDTO", boxPageDTO);

        // 우측 전당 관련
        List<HallDTO> boxHallDTOList = hpService.getTop10BoxHallDTO();
        List<BoxHistoryDTO> boxHistoryDTOList = boxHistoryService.getTop10RecentHistoryDTO();

        model.addAttribute("boxHallDTOList", boxHallDTOList);
        model.addAttribute("boxRecentDTOList", boxHistoryDTOList);

        // 좌측 전당 관련
        LocalDateTime[] range = leftHallService.getDailyRange();
        List<LeftHallDTO> leftHallDTOList = leftHallService.getDailyRanking();

        model.addAttribute("range", range);
        model.addAttribute("leftHallDTOList", leftHallDTOList);

        return "game/box-history";
    }
}
