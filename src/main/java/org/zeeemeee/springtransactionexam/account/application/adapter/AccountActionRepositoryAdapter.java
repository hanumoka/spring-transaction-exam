package org.zeeemeee.springtransactionexam.account.application.adapter;

import org.zeeemeee.springtransactionexam.account.application.domain.AccountAction;

import java.util.Optional;

public interface AccountActionRepositoryAdapter {

    Optional<AccountAction> findAccountActionByAccountId(long accountId);

    AccountAction create(AccountAction accountAction);

    AccountAction update(AccountAction accountAction);
}
