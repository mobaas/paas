/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mobaas.paas.model.TaskInfo;

import java.util.List;

/**
 * 
 * @author billy zhang
 * 
 */
public interface TaskMapper {
	
	List<TaskInfo> selectTaskList();

}
