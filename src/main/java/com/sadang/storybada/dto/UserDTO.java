package com.sadang.storybada.dto;

import com.sadang.storybada.name.domain.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Getter
public class UserDTO {

    private long id;

    private String loginId;
    private String email;
    private LocalDateTime createdAtUser;
    private LocalDateTime updatedAtUser;

    private List<Name> nameList;
    private Long mainNameId;
    private String mainName;

    private Long point;

}
