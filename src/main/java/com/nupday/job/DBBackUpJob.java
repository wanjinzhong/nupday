package com.nupday.job;
import com.nupday.service.DBService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class DBBackUpJob implements Job {

    @Autowired
    private DBService dbService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dbService.backUpDB();
    }
}
