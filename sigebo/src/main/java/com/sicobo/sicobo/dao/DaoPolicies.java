package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanPolicies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoPolicies  extends JpaRepository<BeanPolicies, Long> {
}
