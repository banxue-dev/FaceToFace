package com.general.repository;

import com.general.domain.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author L
 * @date 2018-12-26
 */
public interface EmailRepository extends JpaRepository<EmailConfig,Long> {
}
