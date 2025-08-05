package com.sadang.storybada.game.dice.service;

import com.sadang.storybada.game.dice.domain.DiceHall;
import com.sadang.storybada.game.dice.repository.DiceHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiceHallService {

    private final DiceHallRepository diceHallRepository;

    public DiceHall addDiceHall(long recentGameNumber, long nameId, boolean result) {

        return diceHallRepository.save(DiceHall.builder().gameId(recentGameNumber).nameId(nameId).result(result).build());
    }

    public long getDiceCountByGameId(long game) {

        return diceHallRepository.countByGameIdAndResult(game, true);
    }
}
