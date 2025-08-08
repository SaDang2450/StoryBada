package com.sadang.storybada.hall.repository;

import com.sadang.storybada.hall.domain.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findTop10ByCreatedAtBetweenOrderByHpDesc(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<Hall> findTop200ByCreatedAtBetweenOrderByHp(LocalDateTime localDateTime, LocalDateTime localDateTime1);

    @Query(value = "SELECT * FROM (SELECT * FROM hall WHERE createdAt BETWEEN :localDateTime AND :localDateTime1 ORDER BY hp DESC LIMIT 200) AS limited LIMIT :pageHallCount OFFSET :offset", nativeQuery = true)
    List<Hall> findPagedFromTop200(LocalDateTime localDateTime, LocalDateTime localDateTime1, int pageHallCount, int offset);

    Page<Hall> findAllByUserId(long userId, PageRequest of);
}
