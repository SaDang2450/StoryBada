package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class LottoHistoryDTO {

    private long id;
    private long game;
    private String result;
}
