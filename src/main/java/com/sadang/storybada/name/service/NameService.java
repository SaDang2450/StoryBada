package com.sadang.storybada.name.service;

import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.repository.NameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NameService {

    private final NameRepository nameRepository;
    private final HpService hpService;

    public List<Name> getNameList(long userId) {
        return nameRepository.findByUserId(userId);
    }

    public Long getMainNameId(long userId) {
        Name name = nameRepository.findByUserIdAndIsMain(userId, true);

        return name.getId();
    }

    public String getMainName(long userId) {
        Name name = nameRepository.findByUserIdAndIsMain(userId, true);

        return name.getName();
    }

    public Name addName(String name, long userId) {

        return nameRepository.save(Name.builder().userId(userId).name(name).isMain(true).build());
    }

    public long getCurrentPointByNameId(long nameId) {

        return hpService.getCurrentPointByNameId(nameId);
    }

    public void deleteAllByUserId(long id) {

        List<Name> nameList = nameRepository.findByUserId(id);
        nameRepository.deleteAll(nameList);

        List<Long> nameIdList = new ArrayList<>();
        for (Name name : nameList) {
            nameIdList.add(name.getId());
        }

        hpService.deleteAllByNameId(nameIdList);
    }
}
