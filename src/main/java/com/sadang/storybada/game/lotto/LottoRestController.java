package com.sadang.storybada.game.lotto;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.lotto.service.LottoBufferService;
import com.sadang.storybada.game.lotto.service.LottoHallService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import com.sadang.storybada.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/game/lotto")
@RequiredArgsConstructor
public class LottoRestController {

    private final LottoBufferService lottoBufferService;
    private final LottoHallService lottoHallService;
    private final UserService userService;

    @PostMapping("/buying")
    public ApiResponse<ResponseCode> buyingLotto(HttpSession session, @RequestParam int[] lottoNumberArray) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        // Validation
        ResponseCode responseCode = lottoBufferService.lottoBuyingValidation(userDTO);
        if(!responseCode.equals(ResponseCode.SUCCESS)) {

            return ApiResponse.fail(responseCode);
        }

        // Buying
        Arrays.sort(lottoNumberArray);
        String lotto = Arrays.toString(lottoNumberArray);

        lottoBufferService.addLottoBuffer(userDTO, lotto);

        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @PostMapping("/reload")
    public ApiResponse<Boolean> reloadCurrentUserDTO(HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("myLottoHistory", lottoHallService.getMyLottoHistory(nameId));

        return ApiResponse.success(true);
    }
}
