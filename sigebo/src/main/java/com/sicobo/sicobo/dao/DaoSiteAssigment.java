package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSiteAssigment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoSiteAssigment extends JpaRepository<BeanSiteAssigment,Long> {

    boolean existsBeanSiteAssigmentByBeanSite_Id(Long id);

    BeanSiteAssigment findByBeanUser_IdAndStatus(Long id, int status);
}
