package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DaoPayment  extends JpaRepository<BeanPayment, Long> {

    boolean existsBeanPaymentByBeanWarehouseIdAndStatusIs(Long id, int status);

    boolean existsBeanPaymentByBeanWarehouseIdAndBeanUserId(Long idWarehouse, Long idUser);

    boolean existsBeanPaymentByBeanWarehouseIdAndBeanUserIdAndStatusIs(Long idWarehouse, Long idUser, int status);

    boolean existsBeanPaymentByIdAndStatusIs(Long id, int status);
    boolean existsBeanPaymentByIdAndBeanUserId(Long id, Long idUser);


    List<BeanPayment> findAllByStatusIs(int status);


    @Query(value = "select distinct concat(users.name,' ',users.lastname,' ', users.surname) as fullName, payment.due_date, payment.payment_date,\n" +
            "payment.amount_months, payment.status, warehouse.section, warehouse.final_cost, site.name, state.name\n" +
            "from users \n" +
            "inner join payment on users.id = payment.user_id\n" +
            "inner join warehouse on payment.warehouse_id = warehouse.id\n" +
            "inner join warehouses_type on warehouse.warehouses_types = warehouses_type.id\n" +
            "inner join site on warehouse.site_id_site = site.id\n" +
            "inner join state on site.states_id = state.id\n" +
            "order by payment.status desc\n", nativeQuery = true)
    List<Object[]> findAllPayments();


}
