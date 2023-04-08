package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.model.BeanWarehouseDetails;
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

    @Query("SELECT w.finalCost,w.section, w.description, wi.secureUrl, wt.description "+ "as " + "costo" +", s.address," +
            " st.name  FROM BeanWarehouse w inner join BeanWarehouseImage wi on wi.id = w.id inner join" +
            " BeanWarehousesType wt on w.warehousesType.id = wt.id inner join BeanSite s on" +
            " w.beanSite.id = s.id inner join BeanState st on s.beanState.id = st.id where w.status = 1")
    List<Object[]> findAllWarehouseDetails();
}
