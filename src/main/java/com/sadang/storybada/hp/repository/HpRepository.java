package com.sadang.storybada.hp.repository;

import com.sadang.storybada.hp.domain.Hp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HpRepository extends JpaRepository<Hp, Long> {

    List<Hp> findByNameId(long nameId);
}
