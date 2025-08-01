package com.sadang.storybada.game.lotto.service;

import com.sadang.storybada.game.lotto.repository.LottoHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LottoHistoryService {

    private final LottoHistoryRepository lottoHistoryRepository;


}
