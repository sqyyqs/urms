package com.sqy.urms.persistence.repository;

import com.sqy.urms.persistence.model.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long> {

    boolean existsByValue(String tokenValue);
}
