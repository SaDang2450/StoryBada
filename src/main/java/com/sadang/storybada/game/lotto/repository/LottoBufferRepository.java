package com.sadang.storybada.game.lotto.repository;

import com.sadang.storybada.game.lotto.domain.LottoBuffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LottoBufferRepository extends JpaRepository<LottoBuffer, Long> {

    boolean existsByNameId(Long mainNameId);

    LottoBuffer findByNameId(long nameId);
}
