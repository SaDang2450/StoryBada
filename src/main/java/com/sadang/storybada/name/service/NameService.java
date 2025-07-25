package com.sadang.storybada.name.service;

import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.repository.NameRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NameService {

    private final NameRepository nameRepository;

    public NameService(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    public List<Name> getNameList(long userId) {
        return nameRepository.findByUserId(userId);
    }

    public String getMainName(long userId) {
        Name name = nameRepository.findByUserIdAndIsMain(userId, true);

        return name.getName();
    }

    public boolean addNameOfLoginId(String name, long userId) {

        try {
            nameRepository.save(Name.builder().userId(userId).name(name).isMain(true).build());
        } catch (PersistenceException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
