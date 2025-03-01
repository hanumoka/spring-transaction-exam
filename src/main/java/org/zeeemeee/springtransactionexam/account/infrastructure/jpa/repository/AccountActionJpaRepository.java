package org.zeeemeee.springtransactionexam.account.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zeeemeee.springtransactionexam.account.infrastructure.jpa.entity.AccountActionJpaEntity;

import java.util.Optional;

public interface AccountActionJpaRepository extends JpaRepository<AccountActionJpaEntity, Long> {
    /**
     * 계정 ID로 계정 액션 조회 (기본 방식)
     */
    Optional<AccountActionJpaEntity> findByAccountId(long accountId);

}
