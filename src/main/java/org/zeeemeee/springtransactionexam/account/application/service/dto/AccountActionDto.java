package org.zeeemeee.springtransactionexam.account.application.service.dto;

import lombok.Data;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;

import java.time.LocalDateTime;

@Data
public class AccountActionDto {
    private Long id;

    private Long accountId;

    private Long loginCount;

    private LocalDateTime lastLoginAt;

    private Long logOutCount;

    private LocalDateTime lastLogOutAt;

    private Integer version;

}
