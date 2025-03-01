package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.zeeemeee.springtransactionexam.account.application.service.dto.AccountActionDto;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;
import org.zeeemeee.springtransactionexam.account.application.service.usecase.AccountActionCommandServiceUseCase;

import static org.junit.jupiter.api.Assertions.*;


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

    @Test
    void saveAccountAction() {
        //given
        SaveAccountActionCommand saveAccountActionCommand = new SaveAccountActionCommand();

        //when
        AccountActionDto accountActionDto = accountActionCommandService.saveAccountAction(saveAccountActionCommand);

        //then
        Assertions.assertThat(accountActionDto).isNotNull();
    }
}