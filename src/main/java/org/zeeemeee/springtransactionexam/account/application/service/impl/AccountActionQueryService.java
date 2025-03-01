package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.service.usecase.AccountActionQueryServiceUseCase;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountActionQueryService implements AccountActionQueryServiceUseCase {
    private final AccountActionRepositoryAdapter accountActionRepositoryAdapter;

}
