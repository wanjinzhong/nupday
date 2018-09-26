package com.nupday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * NupDayApplication
 * @author Neil Wan
 * @create 18-8-4
 */
@SpringBootApplication
@EnableTransactionManagement
@EntityScan(basePackages = "com.nupday.dao.entity")
@EnableJpaRepositories(basePackages = "com.nupday.dao.repository")
@EnableAsync
public class NupDayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NupDayApplication.class, args);
    }
}
