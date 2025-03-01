package org.zeeemeee.springtransactionexam.account.application.service.usecase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.zeeemeee.springtransactionexam.account.application.service.dto.AccountActionDto;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;

@Validated
public interface AccountActionCommandServiceUseCase {
    AccountActionDto saveAccountAction(@Valid @NotNull SaveAccountActionCommand command);
}
