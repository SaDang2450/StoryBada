package com.sadang.storybada.hall.service;

import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.repository.HallRepository;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.user.service.UserService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.sadang.storybada.common.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final HpService hpService;
    private final UserService userService;

    public Hall addHall(UserDTO userDTO, String contents, MultipartFile imageFile) {

        String imagePath = FileManager.saveFile(userDTO.getId(), imageFile);

        try {
            hpService.addHpRecord(userDTO.getMainNameId(), (-1) * userDTO.getPoint(), "RegisterHall");
            return hallRepository.save(Hall.builder().userId(userDTO.getId()).name(userDTO.getMainName()).hp(userDTO.getPoint()).contents(contents).imagePath(imagePath).build());
        } catch (PersistenceException e) {
            return null;
        }
    }

    public Hall getHallById(long id) {
        Optional<Hall> optionalHall = hallRepository.findById(id);

        return optionalHall.orElse(null);
    }

    public HallDTO getHallDTOById(long id) {
        Optional<Hall> optionalHall = hallRepository.findById(id);
        if (optionalHall.isPresent()) {
            Hall hall = optionalHall.get();
            return HallDTO.builder().id(hall.getId()).userId(hall.getUserId()).name(hall.getName()).hp(hall.getHp()).contents(hall.getContents()).imagePath(hall.getImagePath())
                    .userLoginId(userService.getLoginIdById(hall.getUserId())).createdAt(hall.getCreatedAt()).updatedAt(hall.getUpdatedAt()).build();
        } else {
            return null;
        }
    }

    public List<HallDTO> getHallDTOList() {
        List<HallDTO> HallDTOList = new ArrayList<>();
        List<Hall> HallList = hallRepository.findAllByOrderByHpDesc();

        long ranking = 1;
        for (Hall hall : HallList) {
            HallDTOList.add(HallDTO.builder().id(hall.getId()).name(hall.getName()).hp(hall.getHp()).ranking(ranking++).createdAt(hall.getCreatedAt()).build());
        }

        return HallDTOList;
    }
}
