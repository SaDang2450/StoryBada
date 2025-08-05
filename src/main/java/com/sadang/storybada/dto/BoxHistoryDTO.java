package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class BoxHistoryDTO {

    private long gameId;
    private long count;

}
