package com.sadang.storybada.name.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.repository.NameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NameService {

    private final NameRepository nameRepository;

    public List<Name> getNameList(long userId) {
        return nameRepository.findByUserId(userId);
    }

    public Long getMainNameId(long userId) {
        Optional<Name> name = nameRepository.findByUserIdAndIsMain(userId, true);

//        if (name.isPresent()) {
//            return name.get().getId();
//        } else {
//            return null;
//        }

        return name.map(Name::getId).orElse(null);
    }

    public String getMainName(long userId) {
        Optional<Name> name = nameRepository.findByUserIdAndIsMain(userId, true);

        return name.map(Name::getName).orElse(null);
    }

    public Name addName(String name, long userId) {

        // 기존 main name 조사 > 있으면 false로 전환
        Optional<Name> optionalCurrentMainName = nameRepository.findByUserIdAndIsMain(userId, true);
        if (optionalCurrentMainName.isPresent()) {
            Name currentMainName = optionalCurrentMainName.get();
            currentMainName = currentMainName.toBuilder().isMain(false).build();
            nameRepository.save(currentMainName);
        }

        // 새로운 name을 main name 으로 생성
        return nameRepository.save(Name.builder().userId(userId).name(name).isMain(true).build());
    }

    public void changeMainName(UserDTO userDTO, long id) {
        long currentMainNameId = userDTO.getMainNameId();

        Optional<Name> optionalCurrentMainName = nameRepository.findById(currentMainNameId);
        if (optionalCurrentMainName.isPresent()) {
            Name currentMainName = optionalCurrentMainName.get();
            currentMainName = currentMainName.toBuilder().isMain(false).build();
            nameRepository.save(currentMainName);
        }

        Optional<Name> optionalNewMainName = nameRepository.findById(id);
        if (optionalNewMainName.isPresent()) {
            Name newMainName = optionalNewMainName.get();
            newMainName = newMainName.toBuilder().isMain(true).build();
            nameRepository.save(newMainName);
        }
    }

    public List<Long> deleteAllByUserId(long id) {

        List<Name> nameList = nameRepository.findByUserId(id);
        nameRepository.deleteAll(nameList);

        List<Long> nameIdList = new ArrayList<>();
        for (Name name : nameList) {
            nameIdList.add(name.getId());
        }

        return nameIdList;
    }

    public boolean duplicationCheck(String name, long userId) {

        Optional<Name> optionalName = nameRepository.findByUserIdAndName(userId, name);
        return optionalName.isEmpty();
    }

    public void deleteNameById(long id) {

        nameRepository.deleteById(id);
    }


    public int howManyNames(long id) {
        List<Name> nameList = nameRepository.findByUserId(id);

        return nameList.size();
    }

    public String getNameById(long nameId) {
        Optional<Name> optionalName = nameRepository.findById(nameId);

        return optionalName.map(Name::getName).orElse(null);
    }
}
