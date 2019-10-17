package com.general.repository;

import com.general.domain.AlipayConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author L
 * @date 2018-12-31
 */
public interface AlipayRepository extends JpaRepository<AlipayConfig,Long> {
}
