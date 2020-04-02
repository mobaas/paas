/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobaas.paas.dao.TaskMapper;
import com.mobaas.paas.model.TaskInfo;
import com.mobaas.paas.service.TaskService;

/**
 * 
 * @author billy zhang
 * 
 */
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskMapper mapper;
	
	@Override
	public List<TaskInfo> selectTaskList() {
		return mapper.selectTaskList();
	}

}
