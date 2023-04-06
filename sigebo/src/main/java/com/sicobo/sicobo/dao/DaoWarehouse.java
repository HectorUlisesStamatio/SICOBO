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

    @Query("SELECT w.finalCost,w.section, w.description, wi.secureUrl, wt.description "+ "as " + "costo" +", s.address," +
            " st.name  FROM BeanWarehouse w inner join BeanWarehouseImage wi on wi.id = w.id inner join" +
            " BeanWarehousesType wt on w.warehousesType.id = wt.id inner join BeanSite s on" +
            " w.beanSite.id = s.id inner join BeanState st on s.beanState.id = st.id where w.status = 1")
    List<Object[]> findAllWarehouseDetails();


    @Query(value = "select * from users \n" +
            "inner join site_assigment on users.id = site_assigment.user_id\n" +
            "inner join site on site_assigment.site_id = site.id\n" +
            "inner join state on site.states_id = state.id\n" +
            "inner join warehouse on site.id = warehouse.site_id_site\n" +
            "inner join warehouses_type on warehouse.warehouses_types = warehouses_type.id\n" +
            "inner join cost_type on warehouses_type.id = cost_type.warehouses_type_id", nativeQuery = true)
    List<Object[]> findAllClienteWarehousesDetails();
}
