package com.sadang.storybada.user.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.user.domain.User;
import com.sadang.storybada.user.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final NameService nameService;

    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, NameService nameService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.nameService = nameService;
    }

    public UserDTO getUser(String loginId, String password) {

        User user = userRepository.findByLoginId(loginId);

        if (user == null) {
            return null;
        }
        if (encoder.matches(password, user.getPassword())) {
            List<Name> nameList = nameService.getNameList(user.getId());
            String mainName = nameService.getMainName(user.getId());

            return UserDTO.builder().id(user.getId()).loginId(user.getLoginId()).email(user.getEmail()).nameList(nameList).mainName(mainName).build();
        }
        else {
            return null;
        }
    }

    public boolean duplicateIdCheck(String loginId) {

        return userRepository.countByLoginId(loginId) == 0;
    }

    public boolean duplicateEmailCheck(String email) {

        return userRepository.countByEmail(email) == 0;
    }

    public boolean addUser(String loginId, String password, String name, String email) {

        String hashingPassword = encoder.encode(password);

        try {
            userRepository.save(User.builder().loginId(loginId).password(hashingPassword).email(email).build());
            return nameService.addNameOfLoginId(name, getIdOfLoginId(loginId));
        } catch (PersistenceException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getIdOfLoginId(String loginId) {

        return userRepository.findByLoginId(loginId).getId();
    }
}
