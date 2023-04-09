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
    boolean existsBeanWarehouseByIdAndStatusIsOrStatusIs(Long id, int status, int otherStatus);

    @Query("SELECT w.finalCost,w.section, w.description, wi.secureUrl, wt.description "+ "as " + "costo" +", s.address," +
            " st.name  FROM BeanWarehouse w inner join BeanWarehouseImage wi on wi.id = w.id inner join" +
            " BeanWarehousesType wt on w.warehousesType.id = wt.id inner join BeanSite s on" +
            " w.beanSite.id = s.id inner join BeanState st on s.beanState.id = st.id where w.status = 1")
    List<Object[]> findAllWarehouseDetails();


    @Query(value = "SELECT DISTINCT concat(u.name,' ',u.lastname,' ', u.surname) as fullName,u.username,\n" +
            "s.name, w.description , w.status, w.final_cost, wt.description\n" +
            "FROM users u \n" +
            "INNER JOIN payment p ON u.id = p.user_id \n" +
            "INNER JOIN warehouse w ON p.warehouse_id = w.id \n" +
            "INNER JOIN site s ON w.site_id_site = s.id \n" +
            "INNER JOIN site_assigment sa ON s.id = sa.site_id \n" +
            "INNER JOIN warehouses_type wt on w.warehouses_types = wt.id\n" +
            "INNER JOIN users u2 ON sa.user_id = u2.id \n" +
            "WHERE u2.id = :id AND s.name = :username and u.role = \"ROLE_USUARIO\"", nativeQuery = true)
    List<Object[]> findAllClienteWarehousesDetails(Long id, String username);


    @Query(value = "select site.name, warehouse.description, warehouses_type.description, warehouse.status, state.name, warehouse_images.url from users\n" +
            "inner join payment on users.id = payment.user_id\n" +
            "inner join warehouse on payment.warehouse_id = warehouse.id\n" +
            "inner join warehouses_type on warehouse.warehouses_types = warehouses_type.id\n" +
            "inner join site on warehouse.site_id_site = site.id\n" +
            "inner join warehouse_images on warehouse_images.warehouse_id = warehouse.id\n" +
            "inner join state on site.states_id = state.id \n" +
            "where users.username = :username", nativeQuery = true)
    List<Object[]> findAllWarehousesByClient(String username);
}
