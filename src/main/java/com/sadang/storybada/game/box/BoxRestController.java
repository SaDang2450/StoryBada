package com.sadang.storybada.game.box;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.box.service.BoxBufferService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class BoxRestController {

    private final BoxBufferService boxBufferService;

    @PostMapping("/box/buying")
    public ApiResponse<ResponseCode> buyingBox(HttpSession session, @RequestParam int amount) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        long nameId = userDTO.getMainNameId();

        for(int i = 0 ; i < amount; i ++) {
            boxBufferService.addBox(nameId);
        }

        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
