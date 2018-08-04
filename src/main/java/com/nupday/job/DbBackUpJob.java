package com.nupday.job;
import com.nupday.service.DbService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DbBackUpJob
 * @author Neil Wan
 * @create 18-8-4
 */
public class DbBackUpJob implements Job {

    @Autowired
    private DbService dbService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dbService.backUpDB();
    }
}
