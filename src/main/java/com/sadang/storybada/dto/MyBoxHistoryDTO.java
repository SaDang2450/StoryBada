package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class MyBoxHistoryDTO {          // boxHall 객체 참조함

    private long id;
    private long gameId;
    private long nameId;
    private long result;

    @DateTimeFormat(pattern="YYYY-MM-DD HH:mm")
    private LocalDateTime createdAt;

    private long count;
}
