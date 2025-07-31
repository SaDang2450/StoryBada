package com.sadang.storybada.game.box.service;

import com.sadang.storybada.game.box.domain.BoxHistory;
import com.sadang.storybada.game.box.repository.BoxHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoxHistoryService {

    private final BoxHistoryRepository boxHistoryRepository;

    public BoxHistory addBoxHistory(long game, String[] results) {

        return boxHistoryRepository.save(BoxHistory.builder().game(game).result1(results[0]).result2(results[1]).result3(results[2]).result4(results[3]).build());
    }
}
