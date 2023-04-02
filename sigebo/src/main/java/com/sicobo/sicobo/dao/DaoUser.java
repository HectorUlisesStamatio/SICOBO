package com.sicobo.sicobo.dao;


import com.sicobo.sicobo.model.BeanUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DaoUser  extends JpaRepository<BeanUser, Long> {
    BeanUser findByUsername(String username);
    boolean existsBeanUserByUsername(String username);
    boolean existsBeanUserByEmail(String email);
    boolean existsBeanUserByUsernameAndIdNot(String username,Long id);
    boolean existsBeanUserByEmailAndIdNot(String email,Long id);
    boolean existsBeanUserById(Long id);
    List<BeanUser> findAllByRoleEquals(String role);
    Optional<BeanUser> findBeanUserById(Long id);
    BeanUser findByEmail(String email);
    BeanUser findBeanUserByTokenPassword(String token);
}
