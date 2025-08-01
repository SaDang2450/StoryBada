package com.sadang.storybada.game.lotto.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.game.lotto.domain.LottoBuffer;
import com.sadang.storybada.game.lotto.repository.LottoBufferRepository;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LottoBufferService {

    private final LottoBufferRepository lottoBufferRepository;
    private final HpService hpService;

    public LottoBuffer addLottoBuffer(UserDTO userDTO, String lotto) {

        long nameId = userDTO.getMainNameId();
        hpService.addHpRecord(nameId, -1000, "Buying Lotto");

        return lottoBufferRepository.save(LottoBuffer.builder().nameId(nameId).lotto(lotto).build());
    }

    public ResponseCode lottoBuyingValidation(UserDTO userDTO) {

        if(userDTO.getPoint() < 1000) {

            return ResponseCode.POINT_NOT_ENOUGH;
        }

        if(lottoBufferRepository.existsByNameId(userDTO.getMainNameId())) {

            return ResponseCode.LOTTO_ALREADY_BOUGHT;
        }

        return ResponseCode.SUCCESS;
    }
}
