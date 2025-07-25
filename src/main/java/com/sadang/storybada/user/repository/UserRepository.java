package com.sadang.storybada.user.repository;

import com.sadang.storybada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLoginIdAndPassword(String loginId, String password);

    int countByLoginId(String loginId);

    int countByEmail(String email);

    User findByLoginId(String loginId);
}
