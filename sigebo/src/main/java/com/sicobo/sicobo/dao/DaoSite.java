package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DaoSite  extends JpaRepository<BeanSite, Long> {

    boolean existsBeanSiteByName(String name);

    boolean existsBeanSiteById(Long id);

    boolean existsBeanSiteByNameAndIdNot(String name, Long id);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM BeanSiteAssigment a WHERE a.beanUser = :beanUser")
    boolean existsByBeanUser(BeanUser beanUser);

    @Query("SELECT sa.beanSite FROM BeanSiteAssigment sa WHERE sa.beanUser = :beanUser")
    List<BeanSite> findByBeanUser(BeanUser beanUser);

    Optional<BeanSite> findBeanSiteById(Long id);
}
