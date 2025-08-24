package com.sadang.storybada.game.box.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.box.domain.BoxBuffer;
import com.sadang.storybada.game.box.repository.BoxBufferBulkRepository;
import com.sadang.storybada.game.box.repository.BoxBufferRepository;
import com.sadang.storybada.game.dice.domain.DiceBuffer;
import com.sadang.storybada.hp.domain.Hp;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoxBufferService {

    private final BoxBufferRepository boxBufferRepository;
    private final BoxBufferBulkRepository boxBufferBulkRepository;
    private final NameService nameService;
    private final HpService hpService;

    public ResponseCode boxBuyingValidation(UserDTO userDTO, long hp) {

        long currentPoint = hpService.getCurrentPointByNameId(userDTO.getMainNameId());

        if (currentPoint < hp) {

            return ResponseCode.POINT_NOT_ENOUGH;
        }

        return ResponseCode.SUCCESS;
    }

    public void addBox(long nameId, int amount) {
        List<BoxBuffer> boxBufferList = new ArrayList<>();
        for(int i = 0 ; i < amount ; i++) {
            boxBufferList.add(BoxBuffer.builder().nameId(nameId).build());
        }

        boxBufferBulkRepository.saveAll(boxBufferList);
        hpService.addHpRecord(nameId, -1000L * amount, "boxBuying");
    }

    public long getHoldingBoxAmount(long nameId) {

        List<BoxBuffer> boxBufferList = boxBufferRepository.findByNameId(nameId);

        return boxBufferList.size();
    }

    public List<BoxBuffer> getAllBoxBuffer() {

        return boxBufferRepository.findAll();
    }

    public void flushBoxBuffer() {

        boxBufferRepository.deleteAllInBatch();
    }

    public void deleteAllByNameId(List<Long> nameIdList) {
        List<BoxBuffer> boxBufferList = new ArrayList<>();
        for (Long nameId : nameIdList) {
            boxBufferList.addAll(boxBufferRepository.findByNameId(nameId));
        }

        boxBufferRepository.deleteAll(boxBufferList);
    }
}
