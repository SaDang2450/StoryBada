package com.sadang.storybada.game.dice.service;

import com.sadang.storybada.dto.DiceHistoryDTO;
import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.game.dice.domain.DiceHistory;
import com.sadang.storybada.game.dice.repository.DiceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiceHistoryService {

    private final DiceHistoryRepository diceHistoryRepository;
    private final DiceHallService diceHallService;

    public DiceHistory addDiceHistory(long game, boolean result) {

        return diceHistoryRepository.save(DiceHistory.builder().game(game).result(result).build());
    }

    public List<DiceHistoryDTO> getTop10RecentHistoryDTO() {
        List<DiceHistory> diceHistoryList = diceHistoryRepository.findTop10ByOrderByCreatedAtDesc();
        List<DiceHistoryDTO> diceHistoryDTOList = new ArrayList<>();
        for (DiceHistory diceHistory : diceHistoryList) {
            diceHistoryDTOList.add(DiceHistoryDTO.builder().game(diceHistory.getGame()).result(diceHistory.isResult()).count(diceHallService.getDiceCountByGameId(diceHistory.getGame())).build());
        }

        return diceHistoryDTOList;
    }

    public boolean getVeryRecentResult() {

        DiceHistory theRecentResult = diceHistoryRepository.findTop1ByOrderByCreatedAtDesc();
        return theRecentResult.isResult();
    }
}
