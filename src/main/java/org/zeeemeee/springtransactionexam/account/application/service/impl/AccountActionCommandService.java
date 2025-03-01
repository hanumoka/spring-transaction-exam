package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.service.domain.AccountAction;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;
import org.zeeemeee.springtransactionexam.account.application.service.usecase.AccountActionCommandServiceUseCase;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountActionCommandService implements AccountActionCommandServiceUseCase {
    private final AccountActionRepositoryAdapter accountActionRepositoryAdapter;


    @Retryable(
            value = {DataIntegrityViolationException.class, ObjectOptimisticLockingFailureException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 100)
    )
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public AccountAction saveAccountAction(SaveAccountActionCommand command) {
        try {
            log.debug("Attempting to find AccountAction for accountId: {}", command.getAccountId());
            Optional<AccountAction> savedData = accountActionRepositoryAdapter.findAccountActionByAccountId(command.getAccountId());

            if (savedData.isEmpty()) {
                log.debug("No existing AccountAction found, creating new one for accountId: {}", command.getAccountId());
                AccountAction accountAction = AccountAction.create(command);
                return accountActionRepositoryAdapter.create(accountAction);
            } else {
                log.debug("Found existing AccountAction, updating for accountId: {}", command.getAccountId());
                AccountAction savedAccountAction = savedData.get();
                AccountAction accountAction = savedAccountAction.update(command);
                return accountActionRepositoryAdapter.update(accountAction);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("Caught DataIntegrityViolationException for accountId: {}, attempting to update instead", command.getAccountId());

            // 예외가 발생했다는 것은 다른 스레드가 이미 레코드를 생성했다는 의미입니다.
            // 따라서 다시 조회하여 업데이트를 시도합니다.
            Optional<AccountAction> existingAction = accountActionRepositoryAdapter.findAccountActionByAccountId(command.getAccountId());

            if (existingAction.isPresent()) {
                AccountAction savedAccountAction = existingAction.get();
                AccountAction accountAction = savedAccountAction.update(command);
                return accountActionRepositoryAdapter.update(accountAction);
            } else {
                // 여전히 레코드를 찾을 수 없는 경우(매우 드문 경우)
                log.error("Unable to find AccountAction after DataIntegrityViolationException for accountId: {}", command.getAccountId());
                throw e;
            }
        }
    }

    @Recover
    public AccountAction recoverSaveAccountAction(Exception e, SaveAccountActionCommand command) {
        // 로깅: 모든 재시도 실패에 대한 정보
        log.error("모든 재시도 후에도 saveAccountAction이 실패했습니다. accountId: {}, 원인: {}",
                command.getAccountId(), e.getMessage(), e);

        Optional<AccountAction> savedData = accountActionRepositoryAdapter.findAccountActionByAccountId(command.getAccountId());

        if (savedData.isPresent()) {
            // 저장된 데이터가 있으면 반환
            return savedData.get();
        }
        // null 반환
        return null;
    }

}
