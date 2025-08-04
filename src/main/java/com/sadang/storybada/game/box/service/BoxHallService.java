package com.sadang.storybada.game.box.service;

import com.sadang.storybada.dto.MyBoxHistoryDTO;
import com.sadang.storybada.game.box.domain.BoxHall;
import com.sadang.storybada.game.box.repository.BoxHallBulkRepository;
import com.sadang.storybada.game.box.repository.BoxHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoxHallService {

    private final BoxHallRepository boxHallRepository;
    private final BoxHallBulkRepository boxHallBulkRepository;
    private static final int BLOCK_PAGE_NUM_COUNT = 10;     // 블럭에 존재하는 페이지 수
    private static final int PAGE_HISTORY_COUNT = 12;          // 한 페이지에 존재하는 히스토리 수

    public long getAverageGetPoint(long nameId) {

        List<BoxHall> boxHallList = boxHallRepository.findByNameId(nameId);

        if (boxHallList.isEmpty()) {
            return 0;
        }

        long sum = 0;
        for (BoxHall boxHall : boxHallList) {
//            switch ((int) boxHall.getResult()) {
//                case 1:
//                    sum += 2000;
//                    break;
//                case 2:
//                    sum += 1500;
//                    break;
//                case 3:
//                    sum += 1000;
//                    break;
//                case 4:
//                    sum += 500;
//                    break;
//            }
            sum += 500 * (5 - boxHall.getResult());
        }

        return (sum - 1000L * boxHallList.size()) / boxHallList.size();
    }

    public void addBoxHallAll(List<BoxHall> boxHallList) {

        boxHallBulkRepository.saveAll(boxHallList);
    }

    public List<MyBoxHistoryDTO> getMyBoxHisotryDTOListPage(long nameId, int pageNum) {
        Page<BoxHall> page = boxHallRepository.findByNameId(nameId, PageRequest.of(pageNum - 1, PAGE_HISTORY_COUNT, Sort.by(Sort.Order.desc("createdAt"))));

        List<BoxHall> boxHallList = page.getContent();
        List<MyBoxHistoryDTO> myBoxHistoryDTOList = new ArrayList<>();

        if (boxHallList.isEmpty()) {
            return null;
        }

        long totalCount = boxHallRepository.countByNameId(nameId);
        long count = totalCount - ((pageNum - 1) * PAGE_HISTORY_COUNT);
        for (BoxHall boxHall : boxHallList) {
            long rank = boxHall.getResult();
            long result = 500 * (5 - rank);
            MyBoxHistoryDTO myBoxHistoryDTO = MyBoxHistoryDTO.builder().id(boxHall.getId()).gameId(boxHall.getNameId()).nameId(boxHall.getNameId()).result(result).count(count--).createdAt(boxHall.getCreatedAt()).build();
            myBoxHistoryDTOList.add(myBoxHistoryDTO);
        }

        return myBoxHistoryDTOList;
    }

    public Page<BoxHall> getMyHistoryPage(long nameId, int pageNum) {
        return boxHallRepository.findByNameId(nameId, PageRequest.of(pageNum - 1, PAGE_HISTORY_COUNT, Sort.by(Sort.Order.desc("createdAt"))));
    }

    public Map<String, Map> getMyHistoryPageData(long nameId, int pageNum) {
        Page<BoxHall> myBoxHistoryPage = boxHallRepository.findByNameId(nameId, PageRequest.of(pageNum - 1, PAGE_HISTORY_COUNT, Sort.by(Sort.Order.desc("createdAt"))));
        Map<String, Map> resultMap = new HashMap<>();
        Map<String, Boolean> resultBooleanMap = new HashMap<>();
        Map<String, Integer> resultIntegerMap = new HashMap<>();

        int currentPage = myBoxHistoryPage.getNumber();
        int totalPages = myBoxHistoryPage.getTotalPages();

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

//    public Map<String, Object> getPageList(long nameId, int pageNum) {
//        Page<BoxHall> page = boxHallRepository.findByNameId(nameId, PageRequest.of(pageNum - 1, PAGE_HISTORY_COUNT, Sort.by(Sort.Order.desc("createdAt"))));
//
//        int totalPages = page.getTotalPages();
//        int currentPage = page.getNumber() + 1;
//
//        int startPage = ((currentPage - 1) / BLOCK_PAGE_NUM_COUNT) * BLOCK_PAGE_NUM_COUNT + 1;
//        int endPage = startPage + BLOCK_PAGE_NUM_COUNT - 1;
//        if (endPage > totalPages) {
//            endPage = totalPages;
//        }
//
//        boolean hasPrev = startPage > 1;
//        boolean hasNext = endPage < totalPages;
//
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("startPage", startPage);
//        resultMap.put("endPage", endPage);
//        resultMap.put("hasPrev", hasPrev);
//        resultMap.put("hasNext", hasNext);
//        resultMap.put("currentPage", currentPage);
//
//        return resultMap;
//    }
}
