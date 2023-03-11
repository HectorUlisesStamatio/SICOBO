package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoState  extends JpaRepository<BeanState, Long> {
}
