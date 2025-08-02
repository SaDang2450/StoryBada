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
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Object> reloadCurrentUserDTO(HttpSession session) {
        Map<String, Object> resultMap = new HashMap<>();
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");
        UserDTO newUserDTO = userService.reloadCurrentUserDTO(userDTO.getId());
        long nameId = newUserDTO.getMainNameId();
        int[] myLottoHistory = lottoHallService.getMyLottoHistory(nameId);

        session.setAttribute("userDTO", newUserDTO);
        session.setAttribute("myLottoHistory", myLottoHistory);

        resultMap.put("userDTO", newUserDTO);
        resultMap.put("myLottoHistory", myLottoHistory);

        return resultMap;
    }
}
