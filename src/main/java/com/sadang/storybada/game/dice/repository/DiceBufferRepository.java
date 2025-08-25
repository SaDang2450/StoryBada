package com.sadang.storybada.game.dice.repository;

import com.sadang.storybada.game.dice.domain.DiceBuffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiceBufferRepository extends JpaRepository<DiceBuffer, Long> {
    List<DiceBuffer> findByNameId(long nameId);

    List<DiceBuffer> findByNameIdAndResult(Long nameId, boolean result);

    List<DiceBuffer> findTop500ByOrderById();

}
