package com.nupday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.nupday"})
@EntityScan(basePackages = "com.nupday.dao.entity")
@EnableJpaRepositories(basePackages = "com.nupday.dao.repository")
public class NupDayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NupDayApplication.class, args);
    }
}
