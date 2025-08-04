package com.sadang.storybada.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class HallDTO {

    private long id;
    private long userId;
    private String name;
    private long hp;
    private String contents;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userLoginId;
    private long ranking;
}
