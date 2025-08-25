package com.sadang.storybada.game.dice.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.dice.domain.DiceBuffer;
import com.sadang.storybada.game.dice.repository.DiceBufferRepository;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiceBufferService {

    private final DiceBufferRepository diceBufferRepository;
    private final HpService hpService;

    public ResponseCode diceBettingValidation(UserDTO userDTO, String betting, long hp) {

        long currentPoint = hpService.getCurrentPointByNameId(userDTO.getMainNameId());
        boolean result = Objects.equals(betting, "odd");

        if (currentPoint < hp) {

            return ResponseCode.POINT_NOT_ENOUGH;
        }

        if (!diceBufferRepository.findByNameIdAndResult(userDTO.getMainNameId(), !result).isEmpty()) {

            return ResponseCode.DICE_REVERSE_BETTING;
        }

        if (hp <= 0) {

            return ResponseCode.POINT_INVALID_VALUE;
        }

        return ResponseCode.SUCCESS;
    }

    public DiceBuffer addDiceBetting(UserDTO userDTO, String betting, long hp) {

        boolean result = Objects.equals(betting, "odd");
        long nameId = userDTO.getMainNameId();
        hpService.addHpRecord(nameId, hp * (-1), "DiceBetting");

        return diceBufferRepository.save(DiceBuffer.builder().nameId(userDTO.getMainNameId()).result(result).hp(hp).build());
    }

    public long getTotalBettingAmount(long nameId) {

        List<DiceBuffer> diceBufferList = diceBufferRepository.findByNameId(nameId);

        long total = 0;
        for (DiceBuffer diceBuffer : diceBufferList) {
            total += diceBuffer.getHp();
        }

        return total;
    }

    public List<DiceBuffer> getDiceBuffer500() {

        return diceBufferRepository.findTop500ByOrderById();
    }

    public void deleteDiceBufferByList(List<DiceBuffer> diceBufferList) {

        diceBufferRepository.deleteAllInBatch(diceBufferList);
    }

    public void deleteAllByNameId(List<Long> nameIdList) {
        List<DiceBuffer> diceBufferList = new ArrayList<>();
        for (Long nameId : nameIdList) {
            diceBufferList.addAll(diceBufferRepository.findByNameId(nameId));
        }

        diceBufferRepository.deleteAll(diceBufferList);
    }


}
