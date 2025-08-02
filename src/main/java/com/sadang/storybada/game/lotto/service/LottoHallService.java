package com.sadang.storybada.game.lotto.service;

import com.sadang.storybada.game.lotto.domain.LottoHall;
import com.sadang.storybada.game.lotto.repository.LottoHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LottoHallService {

    private final LottoHallRepository lottoHallRepository;

    public LottoHall addLottoHall(Long game, long nameId, int result) {

        return lottoHallRepository.save(LottoHall.builder().gameId(game).nameId(nameId).result(result).build());
    }

    public int[] getMyLottoHistory(long nameId) {
        int[] myLottoHistory = new int[6];

        List<LottoHall> lottoHallList = lottoHallRepository.getByNameId(nameId);
        for(LottoHall lottoHall : lottoHallList) {
            int result = lottoHall.getResult();
            myLottoHistory[result - 1]++;
        }

        return myLottoHistory;
    }
}
