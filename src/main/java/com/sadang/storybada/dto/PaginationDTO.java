package com.sadang.storybada.dto;

import com.sadang.storybada.hall.domain.Hall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {

    private Page<Hall> hallPage;
    private List<HallDTO> hallDTOList;

    private int startPage;
    private int endPage;
    private boolean hasPrevGroup;
    private boolean hasNextGroup;
    private int prevGroupPage;
    private int nextGroupPage;

}
