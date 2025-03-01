package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.zeeemeee.springtransactionexam.account.application.service.domain.AccountAction;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;
import org.zeeemeee.springtransactionexam.account.application.type.AccountActionType;

import java.time.LocalDateTime;


@Tag("integration")
@Testcontainers
@Slf4j
@Sql({
        "/data/schema.sql",
        "/data/data.sql",
})
@ActiveProfiles("test")
@SpringBootTest
class AccountActionCommandServiceTest {

    @Autowired
    private AccountActionCommandService accountActionCommandService;

    @DisplayName("insert 성공")
    @Test
    void insertAccountAction() {
        //given
        SaveAccountActionCommand saveAccountActionCommand = SaveAccountActionCommand.builder()
                .accountId(2L)
                .accountActionType(AccountActionType.LOGIN_IN)
                .actionAt(LocalDateTime.now())
                .build();

        //when
        AccountAction accountAction = accountActionCommandService.saveAccountAction(saveAccountActionCommand);

        //then
        Assertions.assertThat(accountAction).isNotNull();
    }

    @DisplayName("update 성공")
    @Test
    void updateAccountAction() {
        //given
        SaveAccountActionCommand saveAccountActionCommand = SaveAccountActionCommand.builder()
                .accountId(1L)
                .accountActionType(AccountActionType.LOGIN_IN)
                .actionAt(LocalDateTime.now())
                .build();

        //when
        AccountAction accountAction = accountActionCommandService.saveAccountAction(saveAccountActionCommand);

        //then
        Assertions.assertThat(accountAction).isNotNull();
    }
}