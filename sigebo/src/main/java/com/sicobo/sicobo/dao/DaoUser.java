package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoUser  extends JpaRepository<BeanUser, Long> {
    BeanUser findByUsername(String username);
    boolean existsBeanUserByUsername(String username);
    boolean existsBeanUserByEmail(String email);
}
