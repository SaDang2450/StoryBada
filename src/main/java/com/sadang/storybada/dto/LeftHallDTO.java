package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class LeftHallDTO {

    private long id;
    private long ranking;
    private String name;
    private String contents;
}
