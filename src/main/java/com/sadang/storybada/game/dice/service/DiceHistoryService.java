package com.sadang.storybada.game.dice.service;

import com.sadang.storybada.game.dice.domain.DiceHistory;
import com.sadang.storybada.game.dice.repository.DiceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiceHistoryService {

    private final DiceHistoryRepository diceHistoryRepository;

    public DiceHistory addDiceHistory(long game, boolean result) {

        return diceHistoryRepository.save(DiceHistory.builder().game(game).result(result).build());
    }
}
