package com.sadang.storybada.game.dice.repository;

import com.sadang.storybada.game.dice.domain.DiceBuffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiceBufferRepository extends JpaRepository<DiceBuffer, Long> {
    List<DiceBuffer> findByNameId(long nameId);

//    @Query(value="SELECT SUM(hp) FROM dicebuffer WHERE nameid = :nameid", nativeQuery=true)
//    long getTotalBettingAmount(@Param("nameid") long nameid);

}
