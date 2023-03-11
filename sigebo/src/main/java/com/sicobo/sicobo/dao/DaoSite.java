package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoSite  extends JpaRepository<BeanSite, Long> {
}
