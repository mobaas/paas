/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas;

import org.springframework.stereotype.Component;

import com.mobaas.paas.schedule.TaskScheduler;

/**
 * 
 * @author billy zhang
 * 
 */
public class TaskSchedulerFactory {
	
	private TaskScheduler scheduler;

	public TaskScheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(TaskScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
}
