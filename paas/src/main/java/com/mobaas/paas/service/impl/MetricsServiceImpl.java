/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mobaas.paas.model.NodeMetrics;
import com.mobaas.paas.model.PodMetrics;
import com.mobaas.paas.dao.MetricsMapper;
import com.mobaas.paas.service.MetricsService;

@Service
public class MetricsServiceImpl implements MetricsService {

	@Resource
	protected MetricsMapper mapper;

	@Override
	public void insertPodMetrics(PodMetrics usage) {
		mapper.insertPodMetrics(usage);
	}

	@Override
	public List<PodMetrics> selectPodMetricsList(String appId, String podName, String kind, int num) {
		if ("1m".equalsIgnoreCase(kind))
			return mapper.selectPodMetricsList(appId, podName, num);
		
		int interval = 60; //60s
		if ("5m".equalsIgnoreCase(kind)) {
			interval = 300;
		} else if ("15m".equalsIgnoreCase(kind)) {
			interval = 900;
		} else if ("1h".equalsIgnoreCase(kind)) {
			interval = 3600;
		}

		return mapper.selectPodMetricsListForGroup(appId, podName, interval, num);
	}

	@Override
	public void insertNodeMetrics(NodeMetrics usage) {
		mapper.insertNodeMetrics(usage);
	}

	@Override
	public List<NodeMetrics> selectNodeMetricsList(String name, String kind, int num) {
		if ("1m".equalsIgnoreCase(kind))
			return mapper.selectNodeMetricsList(name, num);
			
		int interval = 60;
		if ("5m".equalsIgnoreCase(kind)) {
			interval = 300;
		} else if ("15m".equalsIgnoreCase(kind)) {
			interval = 900;
		} else if ("1h".equalsIgnoreCase(kind)) {
			interval = 3600;
		}

		return mapper.selectNodeMetricsListForGroup(name, interval, num);
	}

	@Override
	public int deletePodMetricsBeforeTime(Date time) {
		return mapper.deletePodMetricsBeforeTime(time);
	}

	@Override
	public int deleteNodeMetricsBeforeTime(Date time) {
		return mapper.deleteNodeMetricsBeforeTime(time);
	}

	@Override
	public NodeMetrics selectNodeMetricsLast(String nodeName) {
		return mapper.selectNodeMetricsLast(nodeName);
	}

}
