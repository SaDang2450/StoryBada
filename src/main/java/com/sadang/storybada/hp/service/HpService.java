package com.sadang.storybada.hp.service;

import com.sadang.storybada.dto.HallDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.hp.domain.Hp;
import com.sadang.storybada.hp.repository.HpBulkRepository;
import com.sadang.storybada.hp.repository.HpRepository;
import com.sadang.storybada.name.service.NameService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HpService {

    private final HpRepository hpRepository;
    private final HpBulkRepository hpBulkRepository;
    private final NameService nameService;

    public Hp addHpRecord(long nameId, long point, String game) {

        return hpRepository.save(Hp.builder().nameId(nameId).point(point).game(game).build());
    }

    public void addHpRecordAll(List<Hp> hpRecordList) {

        hpBulkRepository.saveAll(hpRecordList);
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

    public void deleteAllByNameId(List<Long> nameIdList) {
        List<Hp> hpList = new ArrayList<>();
        for (Long nameId : nameIdList) {
            hpList.addAll(hpRepository.findByNameId(nameId));
        }

        hpRepository.deleteAll(hpList);
    }

    @Transactional
    public void deleteHpByNameId(long nameId) {

        hpRepository.deleteByNameId(nameId);
    }

    public List<HallDTO> getTop10DiceHallDTO() {
        List<Hp> diceHpList = hpRepository.findTop10ByGameOrderByPointDescCreatedAtDesc("DiceGame");
        List<HallDTO> hallDTOList = new ArrayList<>();

        int ranking = 1;
        for (Hp hp : diceHpList) {
            hallDTOList.add(HallDTO.builder().ranking(ranking++).name(nameService.getNameById(hp.getNameId())).hp(hp.getPoint()).build());
        }

        return hallDTOList;
    }

    public List<HallDTO> getTop10BoxHallDTO() {
        List<Hp> boxHpList = hpRepository.findTop10ByGameOrderByPointDescCreatedAtDesc("BoxGame");
        List<HallDTO> hallDTOList = new ArrayList<>();

        int ranking = 1;
        for (Hp hp : boxHpList) {
            hallDTOList.add(HallDTO.builder().ranking(ranking++).name(nameService.getNameById(hp.getNameId())).hp(hp.getPoint()).build());
        }

        return hallDTOList;
    }
}
