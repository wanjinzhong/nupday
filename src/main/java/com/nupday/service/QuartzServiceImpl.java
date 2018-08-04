package com.nupday.service;
import com.nupday.job.DbBackUpJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * QuartzServiceImpl
 * @author Neil Wan
 * @create 18-8-4
 */
@Service
public class QuartzServiceImpl implements QuartzService{

    @Autowired
    private Scheduler scheduler;

    private static final String BACK_UP_DB_JOB_NAME = "backUpDBJob";

    private static final String BACK_UP_DB_JOB_GROUP = "backUpDBGroup";

    private final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

    @Override
    public void startBackUpDB() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(DbBackUpJob.class).withIdentity(BACK_UP_DB_JOB_NAME, BACK_UP_DB_JOB_GROUP).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 9 * * ?");
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(BACK_UP_DB_JOB_NAME, BACK_UP_DB_JOB_GROUP).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);

        } catch (SchedulerException e) {
            logger.error("ERROR: " + e.getMessage(), e);
        }
    }
}
