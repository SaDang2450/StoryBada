package com.sadang.storybada.game.dice.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.dice.domain.DiceBuffer;
import com.sadang.storybada.game.dice.repository.DiceBufferRepository;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.name.service.NameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiceBufferService {

    private final DiceBufferRepository diceBufferRepository;
    private final NameService nameService;
    private final HpService hpService;

    public DiceBuffer insertDiceBetting(UserDTO userDTO, String betting, long hp) {

        long currentPoint = nameService.getCurrentPointByNameId(userDTO.getMainNameId());

        if (currentPoint < hp) {
            return null;
        } else {
            boolean result;
            result = Objects.equals(betting, "odd");
            long nameId = userDTO.getMainNameId();
            hpService.addHpRecord(nameId, hp * (-1), "DiceBetting");

            return diceBufferRepository.save(DiceBuffer.builder().nameId(userDTO.getMainNameId()).result(result).hp(hp).build());
        }
    }

    public long getTotalBettingAmount(long nameId) {

        List<DiceBuffer> diceBufferList = diceBufferRepository.findByNameId(nameId);

        long total = 0;
        for (DiceBuffer diceBuffer : diceBufferList) {
            total += diceBuffer.getHp();
        }

        return total;
    }

    public List<DiceBuffer> getAllBuffer() {

        return diceBufferRepository.findAll();
    }
}
