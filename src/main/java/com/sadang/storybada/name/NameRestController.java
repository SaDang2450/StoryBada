package com.sadang.storybada.name;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.response.ApiResponse;
import com.sadang.storybada.response.ResponseCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/name")
public class NameRestController {

    private final NameService nameService;

    @PostMapping("/create")
    public ApiResponse<ResponseCode> createName(HttpSession session, @RequestParam String name) {
        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        // 중복 여부 확인
        if (nameService.duplicationCheck(name, userDTO.getId())) {
            nameService.addName(name, userDTO.getId());

            return ApiResponse.success(ResponseCode.SUCCESS);
        } else {

            return ApiResponse.fail(ResponseCode.USER_NAME_DUPLICATE);
        }
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
        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
