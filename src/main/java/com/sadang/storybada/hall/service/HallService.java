package com.sadang.storybada.hall.service;

import com.sadang.storybada.hall.domain.Hall;
import com.sadang.storybada.hall.repository.HallRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.sadang.storybada.common.FileManager;

@Service
public class HallService {

    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public Hall addHall(long userId, String contents, MultipartFile imageFile) {

        String imagePath = FileManager.saveFile(userId, imageFile);

        try {
            return hallRepository.save(Hall.builder().userId(userId).contents(contents).imagePath(imagePath).build());
        } catch (PersistenceException e) {
            return null;
        }
    }

}
