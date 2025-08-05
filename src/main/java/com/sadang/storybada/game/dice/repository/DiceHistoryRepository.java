package com.sadang.storybada.game.dice.repository;

import com.sadang.storybada.game.dice.domain.DiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiceHistoryRepository extends JpaRepository<DiceHistory,Long> {

    List<DiceHistory> findTop10ByOrderByCreatedAtDesc();
}
