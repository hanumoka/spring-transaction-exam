package org.zeeemeee.springtransactionexam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class SpringTransactionExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTransactionExamApplication.class, args);
    }

}
