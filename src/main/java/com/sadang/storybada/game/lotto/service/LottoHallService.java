package com.sadang.storybada.game.lotto.service;

import com.sadang.storybada.game.lotto.repository.LottoHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LottoHallService {

    private final LottoHallRepository lottoHallRepository;
}
