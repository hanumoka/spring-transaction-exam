package org.zeeemeee.springtransactionexam.account.application.service.usecase;

import org.zeeemeee.springtransactionexam.account.application.service.dto.AccountActionDto;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;

public interface AccountActionCommandServiceUseCase {

    AccountActionDto saveAccountAction(SaveAccountActionCommand comand);
}
