package org.zeeemeee.springtransactionexam.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        //TODO:
        return () -> Optional.of("SYSTEM");
//        // SecurityContext에서 현재 인증된 사용자 정보를 가져와서 반환
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
//                .map(SecurityContext::getAuthentication)
//                .filter(Authentication::isAuthenticated)
//                .map(Authentication::getName)
//                .or(() -> Optional.of("SYSTEM"));
    }
}
