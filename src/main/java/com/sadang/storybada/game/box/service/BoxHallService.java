package com.sadang.storybada.game.box.service;

import com.sadang.storybada.game.box.domain.BoxHall;
import com.sadang.storybada.game.box.repository.BoxBufferRepository;
import com.sadang.storybada.game.box.repository.BoxHallBulkRepository;
import com.sadang.storybada.game.box.repository.BoxHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxHallService {

    private final BoxHallRepository boxHallRepository;
    private final BoxHallBulkRepository boxHallBulkRepository;

    public long getAverageGetPoint(long nameId) {

        List<BoxHall> boxHallList = boxHallRepository.findByNameId(nameId);

        if(boxHallList.isEmpty()){
            return 0;
        }

        long sum = 0;
        for (BoxHall boxHall : boxHallList) {
//            switch ((int) boxHall.getResult()) {
//                case 1:
//                    sum += 2000;
//                    break;
//                case 2:
//                    sum += 1500;
//                    break;
//                case 3:
//                    sum += 1000;
//                    break;
//                case 4:
//                    sum += 500;
//                    break;
//            }
            sum += 500 * (5 - boxHall.getResult());
        }

        return (sum - 1000L * boxHallList.size()) / boxHallList.size();
    }

    public void addBoxHallAll(List<BoxHall> boxHallList) {

        boxHallBulkRepository.saveAll(boxHallList);
    }
}
