package com.sadang.storybada.game.box;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.box.service.BoxBufferService;
import com.sadang.storybada.game.box.service.BoxHallService;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/box")
@RequiredArgsConstructor
public class BoxRestController {

    private final BoxBufferService boxBufferService;
    private final UserService userService;
    private final HpService hpService;
    private final BoxHallService boxHallService;

    @PostMapping("/buying")
    public ApiResponse<ResponseCode> buyingBox(HttpSession session, @RequestParam int amount) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        long nameId = userDTO.getMainNameId();

        // Validation
        long hp = amount * 1000L;
        ResponseCode responseCode = boxBufferService.boxBuyingValidation(userDTO, hp);
        if (responseCode != ResponseCode.SUCCESS) {

            return ApiResponse.fail(responseCode);
        }

        // Buying
        for(int i = 0 ; i < amount; i ++) {
            boxBufferService.addBox(nameId);
            hpService.addHpRecord(nameId, -1000, "BoxBuying");
        }

        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @PostMapping("/reload")
    public ApiResponse<Boolean> reloadCurrentUserDTO(HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("averageBoxGetPoint", boxHallService.getAverageGetPoint(nameId));

        return ApiResponse.success(true);
    }
}
