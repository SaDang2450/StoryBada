package com.sadang.storybada.game.lotto.repository;

import com.sadang.storybada.game.lotto.domain.LottoHall;
import com.sadang.storybada.game.lotto.domain.LottoHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LottoHallRepository extends JpaRepository<LottoHall, Long> {

    List<LottoHall> getByNameId(long nameId);
}
