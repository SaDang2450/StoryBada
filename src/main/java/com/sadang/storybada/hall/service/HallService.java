package com.sadang.storybada.hall.service;

import com.sadang.storybada.common.FileManager;
import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.LeftHallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.repository.HallRepository;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.user.service.UserService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final HpService hpService;
    private final UserService userService;
    private static final int BLOCK_PAGE_NUM_COUNT = 10;     // 블록에 존재하는 페이지 수
    private static final int PAGE_HALL_COUNT = 10;          // 한 페이지에 노출시킬 전당 글 수

    public Hall addHall(UserDTO userDTO, String contents, MultipartFile imageFile) {

        String imagePath = FileManager.saveFile(userDTO.getId(), imageFile);

        try {
            hpService.addHpRecord(userDTO.getMainNameId(), (-1) * userDTO.getPoint(), "RegisterHall");
            return hallRepository.save(Hall.builder().userId(userDTO.getId()).name(userDTO.getMainName()).hp(userDTO.getPoint()).contents(contents).imagePath(imagePath).build());
        } catch (PersistenceException e) {
            return null;
        }
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

    public Page<Hall> getHallPage(int pageNum) {
        return hallRepository.findAll(PageRequest.of(pageNum - 1, PAGE_HALL_COUNT, Sort.by(Sort.Order.desc("hp"))));
    }

    public List<HallDTO> getHallDTOList(int pageNum) {
        Page<Hall> page = hallRepository.findAll(PageRequest.of(pageNum - 1, PAGE_HALL_COUNT, Sort.by(Sort.Order.desc("hp"))));

        List<Hall> hallList = page.getContent();
        List<HallDTO> hallDTOList = new ArrayList<>();

        if (hallList.isEmpty()) {
            return null;
        }
        long ranking = 1;
        for (Hall hall : hallList) {
            hallDTOList.add(HallDTO.builder().id(hall.getId()).name(hall.getName()).hp(hall.getHp()).ranking(ranking++).createdAt(hall.getCreatedAt()).build());
        }

        return hallDTOList;
    }

    public Map<String, Map> getHallPageData(int pageNum) {
        Page<Hall> page = hallRepository.findAll(PageRequest.of(pageNum - 1, PAGE_HALL_COUNT, Sort.by(Sort.Order.desc("hp"))));
        Map<String, Map> resultMap = new HashMap<>();
        Map<String, Boolean> resultBooleanMap = new HashMap<>();
        Map<String, Integer> resultIntegerMap = new HashMap<>();

        int currentPage = page.getNumber();
        int totalPages = page.getTotalPages();

        if (totalPages == 0) {
            totalPages = 1;
        }

        int currentGroup = currentPage / BLOCK_PAGE_NUM_COUNT;
        int startPage = currentGroup * BLOCK_PAGE_NUM_COUNT + 1;
        int endPage = Math.min(startPage + BLOCK_PAGE_NUM_COUNT - 1, totalPages);

        if (startPage > endPage) {
            endPage = startPage;
        }

        boolean hasPrevGroup = startPage > 1;
        boolean hasNextGroup = endPage < totalPages - 1;

        int prevGroupPage = Math.max(startPage - 1, 1);
        int nextGroupPage = (endPage + 1) >= totalPages ? totalPages - 1 : endPage + 1;

        resultBooleanMap.put("hasPrevGroup", hasPrevGroup);
        resultBooleanMap.put("hasNextGroup", hasNextGroup);

        resultIntegerMap.put("startPage", startPage);
        resultIntegerMap.put("endPage", endPage);
        resultIntegerMap.put("prevGroupPage", prevGroupPage);
        resultIntegerMap.put("nextGroupPage", nextGroupPage);

        resultMap.put("booleanMap", resultBooleanMap);
        resultMap.put("integerMap", resultIntegerMap);

        return resultMap;
    }

    // 일간 주간 월간 랭킹 관련 (left-side-zone)


}
