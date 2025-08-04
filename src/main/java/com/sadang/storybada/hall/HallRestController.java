package com.sadang.storybada.hall;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.service.HallService;
import com.sadang.storybada.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/hall")
public class HallRestController {

    private final HallService hallService;

    public HallRestController(HallService hallService) {
        this.hallService = hallService;
    }

    @PostMapping("/create")
    public ApiResponse<Long> hallCreate(HttpSession session, @RequestParam String contents, @RequestParam(required = false) MultipartFile imageFile) {

        UserDTO userDTO = (UserDTO) session.getAttribute("userDTO");

        Hall hall = hallService.addHall(userDTO, contents, imageFile);

        if(hall == null) {
            return ApiResponse.success(0L);
        } else {
            return ApiResponse.success(hall.getId());
        }
    }
}
