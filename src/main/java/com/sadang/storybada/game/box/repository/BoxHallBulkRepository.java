package com.sadang.storybada.game.box.repository;

import com.sadang.storybada.game.box.domain.BoxBuffer;
import com.sadang.storybada.game.box.domain.BoxHall;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoxHallBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<BoxHall> boxHallList) {
        String sql = "INSERT INTO boxhall (gameId, nameId, result) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, boxHallList, boxHallList.size(), (PreparedStatement ps, BoxHall boxHall) -> {
            ps.setLong(1, boxHall.getGameId());
            ps.setLong(2, boxHall.getNameId());
            ps.setLong(3, boxHall.getResult());
        });

    }
}
