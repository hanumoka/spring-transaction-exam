package org.zeeemeee.springtransactionexam.account.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.zeeemeee.springtransactionexam.account.application.service.domain.AccountAction;
import org.zeeemeee.springtransactionexam.common.BaseEntity;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "account_action"
)
@Entity
public class AccountActionJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_action_id_seq")
    @SequenceGenerator(name = "account_action_id_seq", sequenceName = "account_action_id_seq", initialValue = 10000, allocationSize = 1)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "account_id", nullable = false, updatable = false, unique = true)
    private Long accountId;

    @Column(name = "login_count")
    private Long loginCount;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "log_out_count")
    private Long logOutCount;

    @Column(name = "last_log_out_at")
    private LocalDateTime lastLogOutAt;

    public AccountAction toDomain() {
        return AccountAction.builder()
                .id(id)
                .accountId(accountId)
                .loginCount(loginCount)
                .lastLoginAt(lastLoginAt)
                .logOutCount(logOutCount)
                .lastLogOutAt(lastLogOutAt)
                .version(getVersion())
                .build();
    }

    public void update(AccountAction accountAction) {
        this.loginCount = accountAction.getLoginCount();
        this.lastLoginAt = accountAction.getLastLoginAt();
        this.logOutCount = accountAction.getLogOutCount();
        this.lastLogOutAt = accountAction.getLastLogOutAt();
    }
}
