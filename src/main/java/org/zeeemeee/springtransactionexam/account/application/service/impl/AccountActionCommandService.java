package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.service.dto.AccountActionDto;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;
import org.zeeemeee.springtransactionexam.account.application.service.usecase.AccountActionCommandServiceUseCase;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountActionCommandService implements AccountActionCommandServiceUseCase {
    private final AccountActionRepositoryAdapter accountActionRepositoryAdapter;


    @Transactional
    @Override
    public AccountActionDto saveAccountAction(SaveAccountActionCommand comand) {
        return null;
    }
}
