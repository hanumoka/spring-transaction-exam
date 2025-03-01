package org.zeeemeee.springtransactionexam.account.application.service.dto.command;

import lombok.Data;
import org.zeeemeee.springtransactionexam.account.application.type.AccountActionType;

import java.time.LocalDateTime;

@Data
public class SaveAccountActionCommand {
    private long accountActionId;
    private AccountActionType accountActionType;
    private LocalDateTime actionAt;
}
