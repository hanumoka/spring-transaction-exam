package org.zeeemeee.springtransactionexam.account.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zeeemeee.springtransactionexam.account.infrastructure.jpa.entity.AccountActionJpaEntity;

import java.util.Optional;

public interface AccountActionJpaRepository extends JpaRepository<AccountActionJpaEntity, Long> {
    Optional<AccountActionJpaEntity> findByAccountId(long accountId);
}
