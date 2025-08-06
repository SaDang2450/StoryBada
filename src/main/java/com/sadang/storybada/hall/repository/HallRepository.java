package com.sadang.storybada.hall.repository;

import com.sadang.storybada.hall.domain.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findTop10ByCreatedAtBetweenOrderByHpDesc(LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
