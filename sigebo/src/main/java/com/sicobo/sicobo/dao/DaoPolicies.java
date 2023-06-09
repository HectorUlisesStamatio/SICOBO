package com.sicobo.sicobo.dao;

import com.sicobo.sicobo.model.BeanPolicies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaoPolicies  extends JpaRepository<BeanPolicies, Long> {

    Optional<BeanPolicies> findByStatusIs( int status);
}
