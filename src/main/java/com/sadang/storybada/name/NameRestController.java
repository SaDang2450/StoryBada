package com.sadang.storybada.name;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/name")
public class NameRestController {

    private final NameService nameService;
    private final HpService hpService;

    @PostMapping("/create")
    public ApiResponse<ResponseCode> createName(HttpSession session, @RequestParam String name) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        // Validation
        if (!nameService.duplicationCheck(name, userDTO.getId())) {             // 중복 여부 확인

            return ApiResponse.fail(ResponseCode.USER_NAME_DUPLICATE);
        } else if (nameService.howManyNames(userDTO.getId()) >= 10) {           // 10개 제한

            return ApiResponse.fail(ResponseCode.TOO_MANY_NAMES);
        }
        //

        Name registeredName = nameService.addName(name, userDTO.getId());
        hpService.addHpRecord(registeredName.getId(), 10000, "Register");

        return ApiResponse.success(ResponseCode.SUCCESS);

    }

    @PostMapping("/change")
    public ApiResponse<ResponseCode> changeName(HttpSession session, @RequestParam long id) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        nameService.changeMainName(userDTO, id);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @DeleteMapping("/delete")
    public ApiResponse<ResponseCode> deleteName(@RequestParam long id) {

        nameService.deleteNameById(id);
        hpService.deleteHpByNameId(id);

        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
