package com.sadang.storybada.game.box.repository;

import com.sadang.storybada.game.box.domain.BoxHall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoxHallRepository extends JpaRepository<BoxHall,Long> {

    Long countByNameId(long nameId);

    List<BoxHall> findByNameId(long nameId);

    Page<BoxHall> findByNameId(long nameId, Pageable pageable);

    @Query(value = "SELECT gameId, COUNT(*) AS count FROM boxHall GROUP BY gameId ORDER BY gameId DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10GroupedByGameIdNative();
}
