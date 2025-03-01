package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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


    @Transactional
    @Override
    public AccountAction saveAccountAction(SaveAccountActionCommand command) {

        AccountAction result;

        Optional<AccountAction> savedData = accountActionRepositoryAdapter.findAccountActionByAccountId(command.getAccountId());

        if(savedData.isEmpty()){
            AccountAction accountAction = AccountAction.create(command);
            result = accountActionRepositoryAdapter.create(accountAction);
        }else{
            AccountAction savedAccountAcction = savedData.get();
            AccountAction accountAction = savedAccountAcction.update(command);
            result = accountActionRepositoryAdapter.update(accountAction);
        }//else

        return result;
    }
}
