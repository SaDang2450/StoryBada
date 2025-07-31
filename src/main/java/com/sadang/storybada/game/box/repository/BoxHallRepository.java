package com.sadang.storybada.game.box.repository;

import com.sadang.storybada.game.box.domain.BoxHall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxHallRepository extends JpaRepository<BoxHall,Long> {

    List<BoxHall> findByNameId(long nameId);
}
