package com.nupday.service;
import com.nupday.job.DBBackUpJob;
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

@Service
public class QuartzServiceImpl implements QuartzService{

    @Autowired
    private Scheduler scheduler;

    private static final String backUpDBJobName = "backUpDBJob";

    private static final String backUpDBJobGroup = "backUpDBGroup";

    private final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

    @Override
    public void startBackUpDB() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(DBBackUpJob.class).withIdentity(backUpDBJobName, backUpDBJobGroup).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 9 * * ?");
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(backUpDBJobName, backUpDBJobGroup).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);

        } catch (SchedulerException e) {
            logger.error("ERROR: " + e.getMessage(), e);
        }
    }
}
