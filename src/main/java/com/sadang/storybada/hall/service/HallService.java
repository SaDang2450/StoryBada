package com.sadang.storybada.hall.service;

import com.sadang.storybada.common.FileManager;
import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.PaginationDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.repository.HallRepository;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.user.service.UserService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public PaginationDTO getHallPaginationDTO(int pageNum, LocalDateTime[] range, String paper) {
        int offset = (pageNum - 1) * PAGE_HALL_COUNT;
        List<Hall> onlyForTotalPages = hallRepository.findTop200ByCreatedAtBetweenOrderByHp(range[0], range[1]);
        List<Hall> content = hallRepository.findPagedFromTop200(range[0], range[1], PAGE_HALL_COUNT, offset);
        Page<Hall> hallPage = new PageImpl<>(content, PageRequest.of(pageNum - 1, PAGE_HALL_COUNT), 200);

        List<Hall> hallList = hallPage.getContent();
        List<HallDTO> hallDTOList = new ArrayList<>();

        long ranking = (long) (pageNum - 1) * PAGE_HALL_COUNT + 1;
        for (Hall hall : hallList) {
            hallDTOList.add(HallDTO.builder().id(hall.getId()).name(hall.getName()).hp(hall.getHp()).ranking(ranking++).createdAt(hall.getCreatedAt()).build());
        }

        int currentPage = hallPage.getNumber();
        int totalPages = (onlyForTotalPages.size() + PAGE_HALL_COUNT - 1) / PAGE_HALL_COUNT;

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

        return PaginationDTO.builder().hallPage(hallPage).hallDTOList(hallDTOList).startPage(startPage).endPage(endPage)
                .hasPrevGroup(hasPrevGroup).hasNextGroup(hasNextGroup).prevGroupPage(prevGroupPage).nextGroupPage(nextGroupPage).startDate(range[0]).endDate(range[1]).paper(paper).build();
    }

    public PaginationDTO getMyHallPaginationDTO(int pageNum, long userId) {
        Page<Hall> hallPage = hallRepository.findAllByUserId(userId, PageRequest.of(pageNum - 1, PAGE_HALL_COUNT, Sort.by(Sort.Order.desc("createdAt"))));

        List<Hall> hallList = hallPage.getContent();
        List<HallDTO> hallDTOList = new ArrayList<>();

        long ranking = (long) (pageNum - 1) * PAGE_HALL_COUNT + 1;
        for (Hall hall : hallList) {
            hallDTOList.add(HallDTO.builder().id(hall.getId()).name(hall.getName()).hp(hall.getHp()).ranking(ranking++).createdAt(hall.getCreatedAt()).build());
        }

        int currentPage = hallPage.getNumber();
        int totalPages = (hallPage.getNumber() + 1) / PAGE_HALL_COUNT;

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

        return PaginationDTO.builder().hallPage(hallPage).hallDTOList(hallDTOList).startPage(startPage).endPage(endPage)
                .hasPrevGroup(hasPrevGroup).hasNextGroup(hasNextGroup).prevGroupPage(prevGroupPage).nextGroupPage(nextGroupPage).build();
    }
}
