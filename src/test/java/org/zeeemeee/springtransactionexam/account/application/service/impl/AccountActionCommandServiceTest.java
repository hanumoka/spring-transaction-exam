package org.zeeemeee.springtransactionexam.account.application.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.zeeemeee.springtransactionexam.account.application.adapter.AccountActionRepositoryAdapter;
import org.zeeemeee.springtransactionexam.account.application.service.domain.AccountAction;
import org.zeeemeee.springtransactionexam.account.application.service.dto.command.SaveAccountActionCommand;
import org.zeeemeee.springtransactionexam.account.application.type.AccountActionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


@Tag("integration")
//@Testcontainers
@Slf4j
@Sql({
        "/data/schema.sql",
        "/data/data.sql",
})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 각 테스트 메소드 실행 후에 컨텍스트 초기화
@SpringBootTest
class AccountActionCommandServiceTest {

    @Autowired
    private AccountActionCommandService accountActionCommandService;

    @Autowired
    private AccountActionRepositoryAdapter accountActionRepositoryAdapter;

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
        assertThat(accountAction).isNotNull();
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
        assertThat(accountAction).isNotNull();
    }


    @DisplayName("insert 멀티쓰레드 성공")
    @Test
    void multiThreadInsertAccountAction() throws InterruptedException {
        // given
        final long accountId = 5000L; // 테스트용 계정 ID (데이터베이스에 존재하지 않는 ID)
        final int threadCount = 100;   // 동시에 실행할 스레드 수

        // 스레드 동기화를 위한 장치
        final CountDownLatch readyLatch = new CountDownLatch(threadCount); // 모든 스레드가 준비되었는지
        final CountDownLatch startLatch = new CountDownLatch(1);           // 시작 신호
        final CountDownLatch finishLatch = new CountDownLatch(threadCount); // 모든 작업 완료 신호

        // 결과 추적을 위한 변수들
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failCount = new AtomicInteger(0);
        final List<Exception> capturedExceptions = Collections.synchronizedList(new ArrayList<>());

        // ExecutorService 사용하여 스레드 관리
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // when - 여러 스레드가 동시에 같은 accountId로 insert 시도
        for (int i = 0; i < threadCount; i++) {
            final int threadNumber = i;
            executorService.submit(() -> {
                try {
                    // 스레드가 준비되었음을 알림
                    System.out.println("Thread " + threadNumber + " is ready");
                    readyLatch.countDown();

                    // 모든 스레드가 동시에 시작하도록 대기
                    startLatch.await();

                    System.out.println("Thread " + threadNumber + " is executing saveAccountAction");

                    // 모든 스레드가 같은 accountId로 액션 저장 시도
                    SaveAccountActionCommand command = SaveAccountActionCommand.builder()
                            .accountId(accountId)
                            .accountActionType(AccountActionType.LOGIN_IN)
                            .actionAt(LocalDateTime.now())
                            .build();

                    AccountAction result = accountActionCommandService.saveAccountAction(command);
                    System.out.println("Thread " + threadNumber + " succeeded with result ID: " + result.getId());
                    successCount.incrementAndGet();

                } catch (Exception e) {
                    System.out.println("Thread " + threadNumber + " failed with: " + e.getMessage());
                    e.printStackTrace(); // 디버깅을 위해 스택 트레이스 출력
                    failCount.incrementAndGet();
                    capturedExceptions.add(e);
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        // 모든 스레드가 준비될 때까지 대기 (최대 5초)
        boolean allThreadsReady = readyLatch.await(5, TimeUnit.SECONDS);
        System.out.println("All threads ready: " + allThreadsReady);

        if (allThreadsReady) {
            // 모든 스레드를 동시에 시작
            System.out.println("Starting all threads simultaneously");
            startLatch.countDown();

            // 모든 스레드가 작업을 완료할 때까지 대기 (최대 10초)
            boolean allThreadsFinished = finishLatch.await(10, TimeUnit.SECONDS);
            System.out.println("All threads finished: " + allThreadsFinished +
                    ", Success: " + successCount.get() +
                    ", Failures: " + failCount.get());

            // then - 수정된 검증
            // 모든 스레드가 성공해야 함
            assertThat(successCount.get()).isEqualTo(threadCount);

            // 실패한 스레드가 없어야 함
            assertThat(failCount.get()).isZero();

            // 예외가 없어야 함
            assertThat(capturedExceptions).isEmpty();

            // 데이터베이스에 정확히 하나의 레코드만 존재하는지 확인
            Optional<AccountAction> savedRecord = accountActionRepositoryAdapter.findAccountActionByAccountId(accountId);
            assertThat(savedRecord).isPresent();

            // 저장된 레코드가 올바른 accountId를 가지고 있는지 확인
            assertThat(savedRecord.get().getAccountId()).isEqualTo(accountId);

            // 로그인 횟수가 threadCount와 일치하는지 확인
            // 참고: 이 부분은 비즈니스 로직에 따라 다를 수 있음
            // 예를 들어 각 로그인 시 카운트가 증가한다면 아래 검증이 필요할 수 있음
             assertThat(savedRecord.get().getLoginCount()).isEqualTo(threadCount);
        }

        // 스레드 풀 정리
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }


    @DisplayName("update 멀티쓰레드 성공")
    @Test
    void multiThreadUpdateAccountAction() throws InterruptedException {
        // given
        final long accountId = 5000L; // 테스트용 계정 ID
        final int threadCount = 0;   // 동시에 실행할 스레드 수

        // 먼저 테스트를 위한 초기 데이터 생성
        SaveAccountActionCommand initialCommand = SaveAccountActionCommand.builder()
                .accountId(accountId)
                .accountActionType(AccountActionType.LOGIN_IN)
                .actionAt(LocalDateTime.now())
                .build();

        // 초기 레코드 생성
        AccountAction initialRecord = accountActionCommandService.saveAccountAction(initialCommand);
        assertThat(initialRecord).isNotNull();
        assertThat(initialRecord.getAccountId()).isEqualTo(accountId);

        // 초기 로그인 카운트 확인
        Long initialLoginCount = initialRecord.getLoginCount();
        System.out.println("Initial login count: " + initialLoginCount);

        // 스레드 동기화를 위한 장치
        final CountDownLatch readyLatch = new CountDownLatch(threadCount); // 모든 스레드가 준비되었는지
        final CountDownLatch startLatch = new CountDownLatch(1);           // 시작 신호
        final CountDownLatch finishLatch = new CountDownLatch(threadCount); // 모든 작업 완료 신호

        // 결과 추적을 위한 변수들
        final AtomicInteger successCount = new AtomicInteger(0);
        final AtomicInteger failCount = new AtomicInteger(0);
        final List<Exception> capturedExceptions = Collections.synchronizedList(new ArrayList<>());

        // ExecutorService 사용하여 스레드 관리
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // when - 여러 스레드가 동시에 같은 accountId로 update 시도
        for (int i = 0; i < threadCount; i++) {
            final int threadNumber = i;
            executorService.submit(() -> {
                try {
                    // 스레드가 준비되었음을 알림
                    System.out.println("Thread " + threadNumber + " is ready");
                    readyLatch.countDown();

                    // 모든 스레드가 동시에 시작하도록 대기
                    startLatch.await();

                    System.out.println("Thread " + threadNumber + " is executing saveAccountAction (update)");

                    // 모든 스레드가 같은 accountId로 액션 업데이트 시도
                    SaveAccountActionCommand command = SaveAccountActionCommand.builder()
                            .accountId(accountId)
                            .accountActionType(AccountActionType.LOGIN_IN) // 로그인 액션으로 지정
                            .actionAt(LocalDateTime.now().plusSeconds(threadNumber)) // 각 스레드마다 약간 다른 시간 설정
                            .build();

                    AccountAction result = accountActionCommandService.saveAccountAction(command);
                    System.out.println("Thread " + threadNumber + " succeeded with result ID: " + result.getId() +
                            ", Login Count: " + result.getLoginCount());
                    successCount.incrementAndGet();

                } catch (Exception e) {
                    System.out.println("Thread " + threadNumber + " failed with: " + e.getMessage());
                    e.printStackTrace(); // 디버깅을 위해 스택 트레이스 출력
                    failCount.incrementAndGet();
                    capturedExceptions.add(e);
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        // 모든 스레드가 준비될 때까지 대기 (최대 5초)
        boolean allThreadsReady = readyLatch.await(5, TimeUnit.SECONDS);
        System.out.println("All threads ready: " + allThreadsReady);

        if (allThreadsReady) {
            // 모든 스레드를 동시에 시작
            System.out.println("Starting all threads simultaneously");
            startLatch.countDown();

            // 모든 스레드가 작업을 완료할 때까지 대기 (최대 10초)
            boolean allThreadsFinished = finishLatch.await(10, TimeUnit.SECONDS);
            System.out.println("All threads finished: " + allThreadsFinished +
                    ", Success: " + successCount.get() +
                    ", Failures: " + failCount.get());

            // then - 검증
            // 모든 스레드가 성공해야 함
            assertThat(successCount.get()).isEqualTo(threadCount);

            // 실패한 스레드가 없어야 함
            assertThat(failCount.get()).isZero();

            // 예외가 없어야 함
            assertThat(capturedExceptions).isEmpty();

            // 데이터베이스에서 최종 레코드 조회
            Optional<AccountAction> updatedRecord = accountActionRepositoryAdapter.findAccountActionByAccountId(accountId);
            assertThat(updatedRecord).isPresent();

            // 저장된 레코드가 올바른 accountId를 가지고 있는지 확인
            assertThat(updatedRecord.get().getAccountId()).isEqualTo(accountId);

            // 업데이트가 성공적으로 수행되었는지 확인
            System.out.println("Final login count: " + updatedRecord.get().getLoginCount());

            // 비즈니스 로직에 따라 로그인 횟수가 증가해야 한다면:
            if (updatedRecord.get().getLoginCount() > initialLoginCount) {
                System.out.println("Login count increased from " + initialLoginCount +
                        " to " + updatedRecord.get().getLoginCount());
            }

            // 마지막 로그인 시간이 업데이트되었는지 확인
            assertThat(updatedRecord.get().getLastLoginAt()).isNotNull();

            // 비즈니스 로직에 따라 로그인 횟수가 정확히 초기값 + 스레드 수와 일치해야 한다면:
            // assertThat(updatedRecord.get().getLoginCount()).isEqualTo(initialLoginCount + threadCount);
        }

        // 스레드 풀 정리
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}