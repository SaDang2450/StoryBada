package com.sadang.storybada.user.service;

import com.sadang.storybada.dto.NameDTO;
import com.sadang.storybada.dto.UserDTO;
import com.sadang.storybada.email.service.EmailService;
import com.sadang.storybada.hp.service.HpService;
import com.sadang.storybada.name.domain.Name;
import com.sadang.storybada.name.service.NameService;
import com.sadang.storybada.user.domain.User;
import com.sadang.storybada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final NameService nameService;
    private final EmailService emailService;
    private final HpService hpService;

    public UserDTO getUser(String loginId, String password) {

        User user = userRepository.findByLoginId(loginId);

        if (user == null) {
            return null;
        }
        if (encoder.matches(password, user.getPassword())) {
            List<Name> nameList = nameService.getNameList(user.getId());
            List<NameDTO> nameDTOList = new ArrayList<>();

            String mainName = nameService.getMainName(user.getId());
            long nameId = nameService.getMainNameId(user.getId());
            long currentPoint = nameService.getCurrentPointByNameId(nameId);

            for(Name name : nameList){
                long nameCurrentPoint = nameService.getCurrentPointByNameId(name.getId());
                nameDTOList.add(NameDTO.builder().id(name.getId()).userId(name.getUserId()).name(name.getName()).isMain(name.isMain()).currentPoint(nameCurrentPoint).build());
            }

            return UserDTO.builder().id(user.getId()).loginId(user.getLoginId()).email(user.getEmail()).nameDTOList(nameDTOList).mainNameId(nameId).mainName(mainName).point(currentPoint).build();
        } else {
            return null;
        }
    }

    // 우선은 전체 정보 갱신으로 구현했으나, 이후 상황 봐서 포인트 갱신만 필요함이 판명나면 수정.
    public UserDTO reloadCurrentUserDTO(long id) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<Name> nameList = nameService.getNameList(user.getId());
            List<NameDTO> nameDTOList = new ArrayList<>();

            String mainName = nameService.getMainName(user.getId());
            long nameId = nameService.getMainNameId(user.getId());
            long currentPoint = nameService.getCurrentPointByNameId(nameId);

            for(Name name : nameList){
                long nameCurrentPoint = nameService.getCurrentPointByNameId(name.getId());
                nameDTOList.add(NameDTO.builder().id(name.getId()).userId(name.getUserId()).name(name.getName()).isMain(name.isMain()).currentPoint(nameCurrentPoint).build());
            }

            return UserDTO.builder().id(user.getId()).loginId(user.getLoginId()).email(user.getEmail()).nameDTOList(nameDTOList).mainNameId(nameId).mainName(mainName).point(currentPoint).build();
        }

        return null;
    }

    public boolean duplicateIdCheck(String loginId) {

        return userRepository.countByLoginId(loginId) == 0;
    }

    public boolean duplicateEmailCheck(String email) {

        return userRepository.countByEmail(email) == 0;
    }

    public User addUser(String loginId, String password, String name, String email) {

        String hashingPassword = encoder.encode(password);

        User registeredUser = userRepository.save(User.builder().loginId(loginId).password(hashingPassword).email(email).build());
        nameService.addName(name, registeredUser.getId());

        return registeredUser;
    }

    public boolean findPasswordByEmail(String loginId, String email) {
        Optional<User> optionalUser = userRepository.findByLoginIdAndEmail(loginId, email);

        if (optionalUser.isPresent()) {
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

    public Boolean updatePassword(UserDTO userDTO, String password) {
        long id = userDTO.getId();

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashingPassword = encoder.encode(password);
            user = user.toBuilder().password(hashingPassword).updatedAt(LocalDateTime.now()).build();
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public String makeRandomString() {
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public boolean passwordConfirm(UserDTO userDTO, String password) {
        long id = userDTO.getId();

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return encoder.matches(password, user.getPassword());
        } else {
            return false;
        }
    }

    public void deleteUser(UserDTO userDTO) {
        long id = userDTO.getId();

        // 삭제대상 1 : user Table
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(userRepository::delete);

        // 삭제대상 2 : name Table + HP Table
        nameService.deleteAllByUserId(id);
    }

    public String getLoginIdById(long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getLoginId();
        } else {
            return null;
        }
    }
}
