package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MyBoxHistoryDTO {          // boxHall 객체 참조함

    private long id;
    private long gameId;
    private long nameId;
    private long result;
}
