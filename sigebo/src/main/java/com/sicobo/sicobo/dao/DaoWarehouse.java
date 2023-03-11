package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoWarehouse  extends JpaRepository<BeanWarehouse, Long> {
}
