/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mobaas.paas.model.NodeMetrics;
import com.mobaas.paas.model.PodMetrics;

public interface MetricsMapper {

	void insertPodMetrics(PodMetrics metrics);

	List<PodMetrics> selectPodMetricsList(
			@Param("appId")String appId, 
			@Param("podName")String podName, 
			@Param("limit")int limit);

	List<PodMetrics> selectPodMetricsListForGroup(
			@Param("appId")String appId, 
			@Param("podName")String podName, 
			@Param("interval")int interval,
			@Param("limit")int limit);
	
	int deletePodMetricsBeforeTime(
			@Param("time")Date time);

	void insertNodeMetrics(NodeMetrics metrics);

	List<NodeMetrics> selectNodeMetricsList(
			@Param("name")String name, 
			@Param("limit")int limit);

	int deleteNodeMetricsBeforeTime(
			@Param("time")Date time);

	List<NodeMetrics> selectNodeMetricsListForGroup(
			@Param("name")String name, 
			@Param("interval")int interval, 
			@Param("limit")int limit);

	NodeMetrics selectNodeMetricsLast(
			@Param("name")String nodeName);


}
