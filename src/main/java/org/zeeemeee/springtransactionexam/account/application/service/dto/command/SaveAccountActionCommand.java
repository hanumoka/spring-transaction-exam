package org.zeeemeee.springtransactionexam.account.application.service.dto.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.zeeemeee.springtransactionexam.account.application.type.AccountActionType;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveAccountActionCommand {

    @Positive
    private long accountActionId;

    @Positive
    private long accountId;

    @NotNull
    private AccountActionType accountActionType;

    @NotNull
    private LocalDateTime actionAt;
}
