/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.schedule;

import java.util.Map;

/**
 * 
 * @author billy zhang
 * 
 */
public class DispatchJob {

	public void execute(Map<String, Object> jobData) {
		TaskScheduler scheduler = (TaskScheduler)jobData.get("scheduler");
		scheduler.dispatch();
	}

}
