package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DaoWarehouse  extends JpaRepository<BeanWarehouse, Long> {


    boolean existsBeanWarehouseByBeanSite(BeanSite beanSite);
    boolean existsBeanWarehouseByBeanSiteId(Long id);
    List<BeanWarehouse> findBeanWarehouseByBeanSite(BeanSite beanSite);
    List<BeanWarehouse> findAllByBeanSiteId(Long id);

    boolean existsBeanWarehouseById(Long id);
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM BeanWarehouse a WHERE a.id = :id AND a.status = 2")
    boolean existsBeanWarehouseByIdAndStatusIsRented(Long id);

    Optional<BeanWarehouse> findBeanWarehouseById(Long id);

    boolean existsBeanWarehouseByIdAndStatusIs(Long id, int status);

}
