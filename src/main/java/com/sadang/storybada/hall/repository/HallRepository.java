package com.sadang.storybada.hall.repository;

import com.sadang.storybada.hall.domain.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findAllByOrderByHpDesc();
}
