package com.sadang.storybada.game.box.repository;

import com.sadang.storybada.game.box.domain.BoxBuffer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoxBufferBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<BoxBuffer> boxBufferList) {
        String sql = "INSERT INTO boxbuffer (nameId) VALUES (?)";

        jdbcTemplate.batchUpdate(sql, boxBufferList, boxBufferList.size(), (PreparedStatement ps, BoxBuffer boxBuffer) -> {
            ps.setLong(1, boxBuffer.getNameId());
        });

    }
}
