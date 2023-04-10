package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaoAuthorities extends JpaRepository<BeanAuthorities,Long> {
    Optional<BeanAuthorities> findBeanAuthoritiesById(Long id);
}
