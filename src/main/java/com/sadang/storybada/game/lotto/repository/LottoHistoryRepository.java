package com.sadang.storybada.game.lotto.repository;

import com.sadang.storybada.game.lotto.domain.LottoHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LottoHistoryRepository extends JpaRepository<LottoHistory, Long> {
}
