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

    @Query(value = "SELECT w.final_cost,w.section,w.description, wi.secure_url, wt.description, s.address, st.name, w.id  FROM warehouse w inner join \n" +
            "warehouse_images wi on wi.warehouse_id = w.id inner join warehouses_type wt on w.warehouses_types = wt.id\n" +
            "inner join site s on w.site_id_site = s.id inner join state st on s.states_id = st.id where w.status = 1", nativeQuery = true)
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


    @Query(value = "select \n" +
            "site.name,\n" +
            "    warehouse.description,\n" +
            "    warehouses_type.description,\n" +
            "    warehouse.status,\n" +
            "    state.name,\n" +
            "    (SELECT url FROM warehouse_images WHERE warehouse_id = warehouse.id LIMIT 1) as warehouseImageUrl,\n" +
            "    payment.id,\n" +
            "    payment.due_date,\n" +
            "    payment.payment_date,\n" +
            "    payment.status\n" +
            "FROM\n" +
            "    users\n" +
            "    INNER JOIN payment ON users.id = payment.user_id\n" +
            "    INNER JOIN warehouse ON payment.warehouse_id = warehouse.id\n" +
            "    INNER JOIN warehouses_type ON warehouse.warehouses_types = warehouses_type.id\n" +
            "    INNER JOIN site ON warehouse.site_id_site = site.id\n" +
            "    INNER JOIN state ON site.states_id = state.id\n" +
            "WHERE\n" +
            "    users.username = :username", nativeQuery = true)
    List<Object[]> findAllWarehousesByClient(String username);

    boolean existsBeanWarehouseByBeanSiteIdAndStatusIsNot(Long id, int status);
}
