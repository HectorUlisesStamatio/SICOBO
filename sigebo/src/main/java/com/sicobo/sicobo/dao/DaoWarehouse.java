package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DaoWarehouse  extends JpaRepository<BeanWarehouse, Long> {


    boolean existsBeanWarehouseByBeanSite(BeanSite beanSite);
    boolean existsBeanWarehouseByBeanSite_Id(Long id);
    List<BeanWarehouse> findBeanWarehouseByBeanSite(BeanSite beanSite);
    List<BeanWarehouse> findBeanWarehouseByBeanSite_Id(Long id);



}
