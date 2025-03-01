package org.zeeemeee.springtransactionexam.account.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.service.domain.AccountAction;
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

        AccountActionJpaEntity accountActionJpaEntity = AccountActionJpaEntity.builder()
                .accountId(accountAction.getAccountId())
                .loginCount(accountAction.getLoginCount())
                .lastLoginAt(accountAction.getLastLoginAt())
                .logOutCount(accountAction.getLogOutCount())
                .lastLogOutAt(accountAction.getLastLogOutAt())
                .build();

        AccountActionJpaEntity save = accountActionJpaRepository.save(accountActionJpaEntity);

        return save.toDomain();
    }

    @Override
    public AccountAction update(AccountAction accountAction) {

        Optional<AccountActionJpaEntity> accountActionJpaEntity = accountActionJpaRepository.findByAccountId(accountAction.getAccountId());

        if(accountActionJpaEntity.isPresent()){
            AccountActionJpaEntity entity = accountActionJpaEntity.get();
            entity.update(accountAction);
            AccountActionJpaEntity save = accountActionJpaRepository.save(entity);
            return save.toDomain();
        }//if

        return null;
    }
}
