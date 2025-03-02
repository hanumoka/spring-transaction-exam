package org.zeeemeee.springtransactionexam.account.application.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;
import org.zeeemeee.springtransactionexam.account.application.type.AccountActionType;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountAction {
    private Long id;

    private Long accountId;

    private Long loginCount;

    private LocalDateTime lastLoginAt;

    private Long logOutCount;

    private LocalDateTime lastLogOutAt;

    private Integer version;

    public static AccountAction create(SaveAccountActionCommand command) {
        if (command.getAccountActionType() == AccountActionType.LOGIN_IN) {
            return AccountAction.builder()
                    .accountId(command.getAccountId())
                    .loginCount(1L)
                    .lastLoginAt(command.getActionAt())
                    .build();

        } else if (command.getAccountActionType() == AccountActionType.LOG_OUT) {
            return AccountAction.builder()
                    .accountId(command.getAccountId())
                    .logOutCount(1L)
                    .lastLogOutAt(command.getActionAt())
                    .build();
        }//else if
        return null;
    } // create

    public AccountAction update(SaveAccountActionCommand command) {
        if (command.getAccountActionType() == AccountActionType.LOGIN_IN) {
            this.loginCount += 1;
            this.lastLoginAt = command.getActionAt();
        } else if (command.getAccountActionType() == AccountActionType.LOG_OUT) {
            this.logOutCount += 1;
            this.lastLogOutAt = command.getActionAt();
        } // else if
        return this;
    }
}
