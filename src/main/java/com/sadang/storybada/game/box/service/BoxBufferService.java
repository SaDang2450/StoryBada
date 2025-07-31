package com.sadang.storybada.game.box.service;

import com.sadang.storybada.game.box.domain.BoxBuffer;
import com.sadang.storybada.game.box.repository.BoxBufferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoxBufferService {

    private final BoxBufferRepository boxBufferRepository;

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
