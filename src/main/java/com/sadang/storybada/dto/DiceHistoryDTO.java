package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class DiceHistoryDTO {

    private long id;
    private long game;
    private boolean result;
    private LocalDateTime createdAt;

    private long count;
}
