package com.sadang.storybada.game.box.service;

import com.sadang.storybada.dto.BoxHistoryDTO;
import com.sadang.storybada.game.box.domain.BoxHistory;
import com.sadang.storybada.game.box.repository.BoxHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxHistoryService {

    private final BoxHistoryRepository boxHistoryRepository;
    private final BoxHallService boxHallService;

    public BoxHistory addBoxHistory(long game, String[] results) {

        return boxHistoryRepository.save(BoxHistory.builder().game(game).result1(results[0]).result2(results[1]).result3(results[2]).result4(results[3]).result5(results[4]).build());
    }

    public List<BoxHistoryDTO> getTop10RecentHistoryDTO() {
        List<BoxHistory> boxHistoryList = boxHistoryRepository.findTop10ByOrderByGameDesc();
        List<BoxHistoryDTO> boxHistoryDTOList = new ArrayList<>();
        for (BoxHistory boxHistory : boxHistoryList) {
            String[] result1 = boxHistory.getResult1().split(",");
            String[] result5 = boxHistory.getResult5().split(",");
            int size1 = result1.length;
            int size5 = result5.length;

            boxHistoryDTOList.add(BoxHistoryDTO.builder().gameId(boxHistory.getGame()).count(4L * size1 + size5).build());
        }

        return boxHistoryDTOList;
    }
}
