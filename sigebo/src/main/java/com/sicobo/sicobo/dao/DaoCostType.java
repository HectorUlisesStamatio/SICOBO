package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanCostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DaoCostType  extends JpaRepository<BeanCostType, Long> {


    List<BeanCostType> findAllByStatusIs(int status);

    Optional<BeanCostType> findBeanCostTypeByBeanWarehousesTypeIdAndStatusIs(Long idWarehouseType, int status);
}
