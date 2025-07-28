package com.sadang.storybada.game.dice.service;

import com.sadang.storybada.game.dice.domain.DiceBuffer;
import com.sadang.storybada.game.dice.repository.DiceBufferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiceBufferService {

    private final DiceBufferRepository diceBufferRepository;

    public DiceBuffer insertDiceBetting(long nameId, String betting, long hp) {

        boolean result;
        result = Objects.equals(betting, "odd");

        return diceBufferRepository.save(DiceBuffer.builder().nameId(nameId).result(result).hp(hp).build());
    }

    public long getTotalBettingAmount(long nameId) {

        List<DiceBuffer> diceBufferList = diceBufferRepository.findByNameId(nameId);

        long total = 0;
        for(DiceBuffer diceBuffer : diceBufferList){
            total += diceBuffer.getHp();
        }

        return total;
    }
}
