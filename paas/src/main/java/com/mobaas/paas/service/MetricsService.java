/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import java.util.Date;
import java.util.List;

import com.mobaas.paas.model.NodeMetrics;
import com.mobaas.paas.model.PodMetrics;

public interface MetricsService {
	
	void insertPodMetrics(PodMetrics metrics);

	List<PodMetrics> selectPodMetricsList(String appId, String podName, String kind, int num);

	void insertNodeMetrics(NodeMetrics usage);

	List<NodeMetrics> selectNodeMetricsList(String name, String kind, int num);

	int deletePodMetricsBeforeTime(Date time);

	int deleteNodeMetricsBeforeTime(Date time);

	NodeMetrics selectNodeMetricsLast(String nodeName);

}
