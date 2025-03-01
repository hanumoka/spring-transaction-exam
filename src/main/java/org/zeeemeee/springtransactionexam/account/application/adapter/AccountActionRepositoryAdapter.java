package org.zeeemeee.springtransactionexam.account.application.adapter;

import org.zeeemeee.springtransactionexam.account.application.service.dto.AccountActionDto;

import java.util.Optional;

public interface AccountActionRepositoryAdapter {

    Optional<AccountActionDto> findAccountActionByAccountId(long accountId);
}
