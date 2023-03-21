package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaoWarehouse  extends JpaRepository<BeanWarehouse, Long> {

    boolean existsBeanWarehouseByBeanSite_Id(Long id);

    List<BeanWarehouse> findBeanWarehouseByBeanSite_Id(Long id);
}
