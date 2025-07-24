package com.sadang.storybada.user.repository;

import com.sadang.storybada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByLoginIdAndPassword(String loginId, String password);
}
