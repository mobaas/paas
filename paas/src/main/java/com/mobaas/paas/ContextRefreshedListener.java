package com.mobaas.paas;

import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.mobaas.paas.config.JobsConfig;
import com.mobaas.paas.config.SchedulerConfig;
import com.mobaas.paas.job.JobInfo;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(ContextRefreshedListener.class);
	
	@Autowired
    private SchedulerConfig schedulerConfig;
	
	@Autowired
	private JobsConfig jobsConfig;

    public static AtomicInteger count = new AtomicInteger(0);
    
    private static String TRIGGER_GROUP_NAME = "cluster_trigger";
    private static String JOB_GROUP_NAME = "cluster_job";
    
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 防止重复执行
        if (event.getApplicationContext().getParent() == null && count.incrementAndGet() <= 1) {
            initJobs();
        }
	}

	private void initJobs() {
        Scheduler scheduler = null;
        try {
            scheduler = schedulerConfig.scheduler();

            for (JobInfo jobInfo : jobsConfig.getJobs()) {
	            TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getKey() + "_trig", TRIGGER_GROUP_NAME);
	            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
	            if (null == trigger) {
	                Class clazz = Class.forName(jobInfo.getClazz());
	                JobDetail jobDetail = JobBuilder.newJob(clazz)
	                		.withIdentity(jobInfo.getKey() + "_job", JOB_GROUP_NAME)
	                		.build();
	                trigger = TriggerBuilder.newTrigger()
	                		.withIdentity(jobInfo.getKey() + "_trig", TRIGGER_GROUP_NAME)
	                    .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCron()))
	                    .build();
	                scheduler.scheduleJob(jobDetail, trigger);
	                log.info("Quartz 创建了job: {}", jobDetail.getKey());
	            } else {
	                log.info("job已存在: {}", trigger.getKey());
	            }
            }
            
            scheduler.start();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
	}
}
