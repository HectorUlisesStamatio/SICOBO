package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanWarehouseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaoWarehouseImage  extends JpaRepository<BeanWarehouseImage, Long> {

    List<BeanWarehouseImage> findAllByBeanWarehouseId(Long id);
}
