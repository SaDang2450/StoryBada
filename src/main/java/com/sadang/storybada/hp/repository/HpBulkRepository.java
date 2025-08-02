package com.sadang.storybada.hp.repository;


import com.sadang.storybada.game.box.domain.BoxBuffer;
import com.sadang.storybada.hp.domain.Hp;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HpBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Hp> hpRecordList) {
        String sql = "INSERT INTO hp (nameId, point, game) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, hpRecordList, hpRecordList.size(), (PreparedStatement ps, Hp hp) -> {
            ps.setLong(1, hp.getNameId());
            ps.setLong(2, hp.getPoint());
            ps.setString(3, hp.getGame());
        });

    }
}
