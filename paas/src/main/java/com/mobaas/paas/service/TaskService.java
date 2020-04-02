/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import java.util.List;

import com.mobaas.paas.model.TaskInfo;

/**
 * 
 * @author billy zhang
 * 
 */
public interface TaskService {

	List<TaskInfo> selectTaskList();

}
