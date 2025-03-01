package org.zeeemeee.springtransactionexam.account.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.service.dto.AccountActionDto;
import org.zeeemeee.springtransactionexam.account.infrastructure.jpa.repository.AccountActionJpaRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AccountActionRepository implements AccountActionRepositoryAdapter {

    private final AccountActionJpaRepository accountActionJpaRepository;

    @Override
    public Optional<AccountActionDto> findAccountActionByAccountId(long accountId) {
        return Optional.empty();
    }
}
