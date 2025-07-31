package com.sadang.storybada.game.box.repository;

import com.sadang.storybada.game.box.domain.BoxBuffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoxBufferRepository extends JpaRepository<BoxBuffer,Long> {

    List<BoxBuffer> findByNameId(long nameId);

}
