package com.sadang.storybada.hp.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hp.domain.Hp;
import com.sadang.storybada.hp.repository.HpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HpService {

    private final HpRepository hpRepository;

    public Hp addHpRecord(long nameId, long point, String game) {

        return hpRepository.save(Hp.builder().nameId(nameId).point(point).game(game).build());
    }

    public long getCurrentPointByNameId(long nameId) {

        List<Hp> hpList = hpRepository.findByNameId(nameId);
        if (hpList.isEmpty()) {
            return 0;
        } else {
            long sum = 0;
            for (Hp hp : hpList) {
                sum += hp.getPoint();
            }
            return sum;
        }
    }
}
