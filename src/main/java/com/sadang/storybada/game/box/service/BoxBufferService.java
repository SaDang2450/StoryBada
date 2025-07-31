package com.sadang.storybada.game.box.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.box.domain.BoxBuffer;
import com.sadang.storybada.game.box.repository.BoxBufferRepository;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoxBufferService {

    private final BoxBufferRepository boxBufferRepository;
    private final NameService nameService;

    public ResponseCode boxBettingValidation(UserDTO userDTO, long hp) {

        long currentPoint = nameService.getCurrentPointByNameId(userDTO.getMainNameId());

        if (currentPoint < hp) {

            return ResponseCode.DICE_POINT_NOT_ENOUGH;
        }

        return ResponseCode.SUCCESS;
    }

    public void addBox(long nameId) {

        boxBufferRepository.save(BoxBuffer.builder().nameId(nameId).build());
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
}
