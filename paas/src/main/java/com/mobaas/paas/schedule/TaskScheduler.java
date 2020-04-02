/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.schedule;

import com.mobaas.paas.model.TaskInfo;

/**
 * 
 * @author billy zhang
 * 
 */
public interface TaskScheduler {

	void start() throws Exception;
	
	void dispatch();
	
	void stop();
	
	boolean addTask(TaskInfo task);
	
	boolean updateTask(TaskInfo task);
	
	boolean deleteTask(String taskName);
	
}
