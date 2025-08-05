package com.sadang.storybada.game.dice.repository;

import com.sadang.storybada.game.dice.domain.DiceHall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiceHallRepository extends JpaRepository<DiceHall, Long> {

    long countByGameIdAndResult(long game, boolean b);
}
