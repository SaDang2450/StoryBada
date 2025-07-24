package com.sadang.storybada.name.repository;

import com.sadang.storybada.name.domain.Name;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NameRepository extends JpaRepository<Name, Long> {

    public List<Name> findByUserId(long userId);

    public Name findByUserIdAndIsMain(long userId, boolean b);
}
