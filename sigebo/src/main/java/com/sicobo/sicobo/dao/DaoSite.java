package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaoSite  extends JpaRepository<BeanSite, Long> {

    boolean existsBeanSiteByName(String name);

    boolean existsBeanSiteById(Long id);

    boolean existsBeanSiteByNameAndIdNot(String name, Long id);

    Optional<BeanSite> findBeanSiteById(Long id);
}
