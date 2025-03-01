package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.service.dto.AccountActionDto;
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
    public AccountActionDto saveAccountAction(SaveAccountActionCommand command) {

        AccountActionDto result = null;

        Optional<AccountActionDto> savedData = accountActionRepositoryAdapter.findAccountActionByAccountId(command.getAccountId());

        if(savedData.isEmpty()){
//            AccountActionDto accountActionDto = AccountActionDto.from(command);
//            result = accountActionRepositoryAdapter.create(accountActionDto);
        }else{
//            AccountActionDto accountActionDto = savedData.get();
//            accountActionDto.update(command);
//            result = accountActionRepositoryAdapter.update(accountActionDto);
        }//else

        return result;
    }
}
