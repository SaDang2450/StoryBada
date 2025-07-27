package com.sadang.storybada.user.service;

import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.email.service.EmailService;
import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.user.domain.User;
import com.sadang.storybada.user.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final NameService nameService;
    private final EmailService emailService;

    public UserService(BCryptPasswordEncoder encoder, UserRepository userRepository, NameService nameService, EmailService emailService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.nameService = nameService;
        this.emailService = emailService;
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

    public boolean findPasswordByEmail(String loginId, String email) {
        Optional<User> optionalUser = userRepository.findByLoginIdAndEmail(loginId, email);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            String tempPassword = makeRandomString();
            String hashingTempPassword = encoder.encode(tempPassword);
            user = user.toBuilder().password(hashingTempPassword).updatedAt(LocalDateTime.now()).build();
            userRepository.save(user);

            emailService.findEmail(email, tempPassword);

            return true;
        } else {

            return false;
        }
    }

    public void updatePassword(String loginId, String oldPassword, String newPassword) {
        User user = userRepository.findByLoginId(loginId);


    }

    public String makeRandomString() {
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();

        for(int i = 0 ; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
