package com.sadang.storybada.game.box.repository;

import com.sadang.storybada.game.box.domain.BoxHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxHistoryRepository extends JpaRepository<BoxHistory,Long> {

    List<BoxHistory> findTop10ByOrderByCreatedAtDesc();

    List<BoxHistory> findTop10ByOrderByGameDesc();
}
