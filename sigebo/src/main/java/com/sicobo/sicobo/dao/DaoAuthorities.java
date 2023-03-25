package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoAuthorities extends JpaRepository<BeanAuthorities,Long> {
}
