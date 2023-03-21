package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoPayment  extends JpaRepository<BeanPayment, Long> {

    boolean existsBeanPaymentByBeanWarehouse_IdAndStatusEquals(Long id, int status);

}
