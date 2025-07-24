package com.sadang.storybada.user.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.user.domain.User;
import com.sadang.storybada.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        String encodedPassword = encoder.encode(password);

        User user = userRepository.findByLoginIdAndPassword(loginId, encodedPassword);

        if (user == null) {
            return null;
        } else {
            List<Name> nameList = nameService.getNameList(user.getId());
            String mainName = nameService.getMainName(user.getId());

            return UserDTO.builder().id(user.getId()).loginId(user.getLoginId()).email(user.getEmail()).nameList(nameList).mainName(mainName).build();
        }



    }
}
