package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class NameDTO {

    private long id;
    private long userId;
    private String name;
    private boolean isMain;

    private long currentPoint;

}
