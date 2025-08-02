package com.sadang.storybada.game.lotto.service;

import com.sadang.storybada.game.lotto.domain.LottoHistory;
import com.sadang.storybada.game.lotto.repository.LottoHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LottoHistoryService {

    private final LottoHistoryRepository lottoHistoryRepository;

    public LottoHistory addLottoHistory(long game, String lotto) {

        return lottoHistoryRepository.save(LottoHistory.builder().game(game).lotto(lotto).build());
    }

}
