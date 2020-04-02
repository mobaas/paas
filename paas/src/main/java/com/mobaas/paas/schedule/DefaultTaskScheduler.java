/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.schedule;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.model.TaskInfo;
import com.mobaas.paas.schedule.job.AbstractJob;
import com.mobaas.paas.service.TaskService;


/**
 * 
 * @author billy zhang
 * 
 */
public class DefaultTaskScheduler implements TaskScheduler {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    private static final String JOB_DISPATCH_GROUP_NAME = "JOB_DISPATCH_GROUP_NAME";
    private static final String TRIGGER_DISPATCH_GROUP_NAME = "TRIGGER_DISPATCH_GROUP_NAME";

    private static final String JOB_TASK_GROUP_NAME = "JOB_TASK_GROUP_NAME";
    private static final String TRIGGER_TASK_GROUP_NAME = "TRIGGER_TASK_GROUP_NAME";

    private static final String DISPATCH_NAME = "DISPATCH";
    
    private Logger logger = LoggerFactory.getLogger(DefaultTaskScheduler.class);

    private Map<String, TaskInfo> taskMap = new HashMap<>();
    
    private Scheduler scheduler;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private PaasConfig config;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ObjectMapper jsonMapper;
    
    private Scheduler getScheduler() throws SchedulerException {
    	if (scheduler == null) {
    		scheduler = schedulerFactory.getScheduler();
    	}
    	
    	return scheduler;
    }
    
    @Override
    public void start() throws Exception {
    	
        this.scheduler = getScheduler();
        
        JobDetail jobDetail = getDispatchJobDetail();
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(config.getDispatchCron()))
                .withIdentity(new TriggerKey(DISPATCH_NAME, TRIGGER_DISPATCH_GROUP_NAME))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
       
        logger.info("start dispatch finished.");
        
    }

    private JobDetail getDispatchJobDetail() throws ClassNotFoundException, NoSuchMethodException {
    		Map<String, Object> jobArgs = new HashMap<>();
        jobArgs.put("scheduler", this);
        
        DispatchJob dispJob = new DispatchJob();
        
        MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactory.setName(DISPATCH_NAME);
        jobDetailFactory.setGroup(JOB_DISPATCH_GROUP_NAME);
        jobDetailFactory.setTargetObject(dispJob);
        jobDetailFactory.setTargetMethod("execute");
        jobDetailFactory.setArguments(jobArgs);
        jobDetailFactory.afterPropertiesSet();
        
        return jobDetailFactory.getObject();
    }

    @Override
    public void stop() {
	    	taskMap.clear();
	    	
	    	if (scheduler != null) {
	    		try {
				scheduler.shutdown();
				scheduler = null;
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
	    	}
    }

    @Override
    public void dispatch() {
    	
     	try {
			this.scheduler = getScheduler();
		} catch (SchedulerException e) {
			e.printStackTrace();
			return;
		}
     	
//	    	if (redisLock.setLock("quartz", 3600)) {  // lock 1h
		   
		    	List<TaskInfo> tasklist = taskService.selectTaskList();
		    	scheduleTasks(tasklist);
	    	
//	    } else {
//	    		
//		    	for (TaskInfo ti : taskMap.values()) {
//		    		try {
//				    	if (taskIsScheduled(ti)) {  // 删除任务
//			        		deleteTask(ti.getName());
//			        	}
//		    		} catch (SchedulerException e) {
//		    			e.printStackTrace();
//		    		}
//		    	}
//	    }
	}
    
    private boolean taskIsScheduled(TaskInfo taskInfo) throws SchedulerException {
		JobKey jobKey = getTaskJobKey(taskInfo.getName());
        TriggerKey triggerKey = getTaskTriggerKey(taskInfo.getName());
        
        return scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey);
    }

    private boolean taskIsModified(TaskInfo ti) {
    
	    	if (!taskMap.containsKey(ti.getName()))
	    		return true;
	    	
	    	TaskInfo old = taskMap.get(ti.getName());
	    	
	    	return (old.getModified().compareTo(ti.getModified())) != 0;
    }
    
    public void scheduleTasks(List<TaskInfo> tasklist) {

	    	int addCount = 0;
	    	int updateCount = 0;
	    	int deleteCount = 0;
	    	
	    	for (TaskInfo ti : tasklist) {

	        try {
		        
		        if (ti.getDel() == 1) {
			        	if (taskIsScheduled(ti)) {  // 删除任务
			        		deleteTask(ti.getName());
			        		deleteCount++;
			        	}
			        	continue;
		        }
	        
	            if (taskIsScheduled(ti)) {
		            	if (taskIsModified(ti)) {  // 更新任务
		            		updateTask(ti);
		            		updateCount++;
		            	}
	            } else {
		            	addTask(ti);  // 增加任务
		            	addCount++;
	            }
	            
            		taskMap.put(ti.getName(), ti);
	        } catch (SchedulerException se) {
	        		se.printStackTrace();
	        } catch (TaskException te) {
	        		te.printStackTrace();
	        }
	    	}
        
        logger.info(String.format("dispatch finished. add:%d, update:%d, delete:%d", addCount, updateCount, deleteCount));
    }
    
    public Collection<TaskInfo> getTaskList() {
    		return taskMap.values();
    }
    
    public int getTaskCount() {
	    	if (scheduler != null) {
	    		try {
	    			return scheduler.getJobKeys(GroupMatcher.anyJobGroup()).size();
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
	    	}
	    	
	    	return 0;
    }
    
    private JobKey getTaskJobKey(String name) {
    		return new JobKey(name, JOB_TASK_GROUP_NAME);
    }
    
    private TriggerKey getTaskTriggerKey(String name) {
    		return new TriggerKey(name, TRIGGER_TASK_GROUP_NAME);
    }
    
    @Override
    public boolean addTask(TaskInfo task) {
        boolean result = false;
        if (!CronExpression.isValidExpression(task.getCronExp())) {
            logger.error("Illegal cron expression format({})", task.getCronExp());
            return result;
        }

        try {
	        JobDetail jobDetail = getTaskJobDetail(task);
	        
	        Trigger trigger = TriggerBuilder.newTrigger()
	                .forJob(jobDetail)
	                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExp()))
	                .withIdentity(getTaskTriggerKey(task.getName()))
	                .build();
	        scheduler.scheduleJob(jobDetail, trigger);
	        scheduler.start();
        } catch (Exception ex) {
        		throw new TaskException(ex.getMessage(), ex);
        }
       
        return result;
    }

    private JobDetail getTaskJobDetail(TaskInfo task) throws Exception {
	    	Map<String, Object> jobData = getTaskJobData(task);
	    	
	    	Class<? extends AbstractJob> jobClass = (Class<? extends AbstractJob>)Class.forName(task.getJobClazz());
	    	AbstractJob targetJob = jobClass.newInstance();
	    	context.getAutowireCapableBeanFactory().autowireBean(targetJob);
	        
	    	MethodInvokingJobDetailFactoryBean jobDetailFactory = new MethodInvokingJobDetailFactoryBean();
        jobDetailFactory.setName(task.getName());
        jobDetailFactory.setGroup(JOB_TASK_GROUP_NAME);
        jobDetailFactory.setTargetObject(targetJob);
        jobDetailFactory.setTargetMethod("execute");
        jobDetailFactory.setArguments(jobData);
        jobDetailFactory.afterPropertiesSet();
        
        return jobDetailFactory.getObject();
    }
    
    private Map<String, Object> getTaskJobData(TaskInfo task) throws IOException {
    		if (!StringUtils.isEmpty(task.getJobData())) {
    			return (Map<String, Object>)jsonMapper.readValue(task.getJobData(), Map.class);
    		} else {
    			return new HashMap<String, Object>();
    		}
    }

    @Override
    public boolean updateTask(TaskInfo task) {
        boolean result = false;
        if (!CronExpression.isValidExpression(task.getCronExp())) {
            logger.error("Illegal cron expression format({})", task.getCronExp());
            return result;
        }
        JobKey jobKey = getTaskJobKey(task.getName());
        TriggerKey triggerKey = getTaskTriggerKey(task.getName());
        
        try {
	        Map<String, Object> jobData = getTaskJobData(task);
	
	        if (taskIsScheduled(task)) {
	            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
	            jobDetail.getJobDataMap().clear();
	            jobDetail.getJobDataMap().putAll(jobData);
	            
	            Trigger newTrigger = TriggerBuilder.newTrigger()
	                    .forJob(jobDetail)
	                    .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExp()))
	                    .withIdentity(triggerKey)
	                    .build();
	            scheduler.rescheduleJob(triggerKey, newTrigger);
	            result = true;
	        } else {
	            logger.error("update job name:{},group name:{} or trigger name:{},group name:{} not exists..",
	                    jobKey.getName(), jobKey.getGroup(), triggerKey.getName(), triggerKey.getGroup());
	        }
        } catch (Exception ex) {
        		throw new TaskException(ex.getMessage(), ex);
        }
      
        return result;
    }

    @Override
    public boolean deleteTask(String taskName) {
    		
        boolean result = false;
        JobKey jobKey = getTaskJobKey(taskName);
        
        try {
	        if (scheduler.checkExists(jobKey)) {
	            result = scheduler.deleteJob(jobKey);
	        } else {
	            logger.error("delete job name:{},group name:{} not exists.", jobKey.getName(), jobKey.getGroup());
	        }
        } catch (SchedulerException se) {
        		throw new TaskException(se.getMessage(), se);
        }

        return result;
    }

}
