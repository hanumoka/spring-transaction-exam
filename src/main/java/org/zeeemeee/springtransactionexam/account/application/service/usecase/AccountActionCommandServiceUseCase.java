package org.zeeemeee.springtransactionexam.account.application.service.usecase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.zeeemeee.springtransactionexam.account.application.domain.AccountAction;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;

@Validated
public interface AccountActionCommandServiceUseCase {
    AccountAction saveAccountAction(@Valid @NotNull SaveAccountActionCommand command);
}
