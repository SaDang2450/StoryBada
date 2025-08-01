package com.sadang.storybada.game.lotto.repository;

import com.sadang.storybada.game.lotto.domain.LottoBuffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LottoBufferRepository extends JpaRepository<LottoBuffer, Long> {

    boolean existsByNameId(Long mainNameId);
}
