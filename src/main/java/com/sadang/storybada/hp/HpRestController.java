package com.sadang.storybada.hp;

import com.sadang.storybada.hp.service.HpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HpRestController {

    private final HpService hpService;


}
