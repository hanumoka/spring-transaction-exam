package org.zeeemeee.springtransactionexam.account.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.domain.AccountAction;
import org.zeeemeee.springtransactionexam.account.infrastructure.jpa.entity.AccountActionJpaEntity;
import org.zeeemeee.springtransactionexam.account.infrastructure.jpa.repository.AccountActionJpaRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AccountActionRepository implements AccountActionRepositoryAdapter {

    private final AccountActionJpaRepository accountActionJpaRepository;


    @Override
    public Optional<AccountAction> findAccountActionByAccountId(long accountId) {
        return accountActionJpaRepository.findByAccountId(accountId)
                .map(AccountActionJpaEntity::toDomain);
    }

    @Override
    public AccountAction create(AccountAction accountAction) {

        try {
            AccountActionJpaEntity accountActionJpaEntity = AccountActionJpaEntity.builder()
                    .accountId(accountAction.getAccountId())
                    .loginCount(accountAction.getLoginCount())
                    .lastLoginAt(accountAction.getLastLoginAt())
                    .logOutCount(accountAction.getLogOutCount())
                    .lastLogOutAt(accountAction.getLastLogOutAt())
                    .build();

            AccountActionJpaEntity save = accountActionJpaRepository.save(accountActionJpaEntity);
            log.debug("Successfully created AccountAction with ID: {} for accountId: {}",
                    save.getId(), accountAction.getAccountId());
            return save.toDomain();
        } catch (DataIntegrityViolationException e) {
            log.warn("DataIntegrityViolationException while creating AccountAction for accountId: {}",
                    accountAction.getAccountId());
            throw e; // 상위 계층에서 처리할 수 있도록 예외를 다시 던집니다.
        }
    }

    @Override
    public AccountAction update(AccountAction accountAction) {

        try {
            Optional<AccountActionJpaEntity> accountActionJpaEntityOpt =
                    accountActionJpaRepository.findByAccountId(accountAction.getAccountId());

            if (accountActionJpaEntityOpt.isPresent()) {
                AccountActionJpaEntity entity = accountActionJpaEntityOpt.get();
                entity.update(accountAction);
                AccountActionJpaEntity save = accountActionJpaRepository.save(entity);
                log.debug("Successfully updated AccountAction with ID: {} for accountId: {}",
                        save.getId(), accountAction.getAccountId());
                return save.toDomain();
            }
            log.warn("Unable to update AccountAction: No entity found for accountId: {}",
                    accountAction.getAccountId());
            return null;
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("OptimisticLockingFailureException while updating AccountAction for accountId: {}",
                    accountAction.getAccountId());
            throw e; // 상위 계층에서 처리할 수 있도록 예외를 다시 던집니다.
        }
    }
}
