package com.sadang.storybada.game.lotto.service;

import com.sadang.storybada.dto.LottoHistoryDTO;
import com.sadang.storybada.game.lotto.domain.LottoHistory;
import com.sadang.storybada.game.lotto.repository.LottoHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LottoHistoryService {

    private final LottoHistoryRepository lottoHistoryRepository;

    public LottoHistory addLottoHistory(long game, String lotto) {

        return lottoHistoryRepository.save(LottoHistory.builder().game(game).lotto(lotto).build());
    }

    public List<LottoHistoryDTO> getTop10RecentHistoryDTO() {
        List<LottoHistory> lottoHistoryList = lottoHistoryRepository.findTop10ByOrderByCreatedAtDesc();
        List<LottoHistoryDTO> lottoHistoryDTOList = new ArrayList<>();
        for (LottoHistory lottoHistory : lottoHistoryList) {
            String[] lotto =  lottoHistory.getLotto().split(",");
            lottoHistoryDTOList.add(LottoHistoryDTO.builder().game(lottoHistory.getGame()).result(lotto).build());
        }

        return lottoHistoryDTOList;

    }
}
