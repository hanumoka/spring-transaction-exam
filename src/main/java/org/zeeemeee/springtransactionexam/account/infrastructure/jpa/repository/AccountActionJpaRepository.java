package org.zeeemeee.springtransactionexam.account.infrastructure.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zeeemeee.springtransactionexam.account.infrastructure.jpa.entity.AccountActionJpaEntity;

public interface AccountActionJpaRepository extends JpaRepository<AccountActionJpaEntity, Long> {
}
