package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanPayment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DaoPayment  extends JpaRepository<BeanPayment, Long> {

    boolean existsBeanPaymentByBeanWarehouseIdAndStatusEquals(Long id, int status);

    boolean existsBeanPaymentByBeanWarehouseIdAndBeanUserId(Long idWarehouse, Long idUser);

    boolean existsBeanPaymentByBeanWarehouseIdAndBeanUserIdAndStatusIs(Long idWarehouse, Long idUser, int status);

    boolean existsBeanPaymentByIdAndStatusIs(Long id, int status);
    boolean existsBeanPaymentByIdAndBeanUserId(Long id, Long idUser);

}
