package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSiteAssigment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoSiteAssigment extends JpaRepository<BeanSiteAssigment,Long> {

    BeanSiteAssigment findByBeanUserId(Long id);

    boolean existsBeanSiteAssigmentByBeanSiteIdAndStatusIs(Long id, int status);
}
