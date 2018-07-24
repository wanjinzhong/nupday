package com.nupday.initialization;

import com.nupday.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class JobInitialization implements CommandLineRunner {
    @Autowired
    private QuartzService quartzService;
    @Override
    public void run(String... args) throws Exception {
        quartzService.startBackUpDB();
    }
}
