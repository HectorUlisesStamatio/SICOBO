package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanWarehousesType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaoWarehousesType  extends JpaRepository<BeanWarehousesType, Long> {
    List<BeanWarehousesType> findAllByBeanCostTypeIsNotNullAndStatusIs(int status);
}
