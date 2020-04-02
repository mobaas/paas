/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.schedule;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.mobaas.paas.TaskSchedulerFactory;

/**
 * 
 * @author billy zhang
 * 
 */
@Component
@ConditionalOnProperty(name="paas.schedule.enable", havingValue="true")
public class SchedulerBean {

	@Autowired
	private TaskSchedulerFactory schedulerFactory;

	private TaskScheduler scheduler;
	
	@PostConstruct
    public void start() throws Exception {
	    	scheduler = schedulerFactory.getScheduler();
	    	
	    	scheduler.start();
	}

    @PreDestroy
    public void stop() {
	    	scheduler.stop();
	    	scheduler = null;
    }

}
