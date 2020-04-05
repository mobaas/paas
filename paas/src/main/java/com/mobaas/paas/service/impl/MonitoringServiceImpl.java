/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobaas.paas.model.DockerInfo;
import com.mobaas.paas.model.Host;
import com.mobaas.paas.model.NodeMetrics;
import com.mobaas.paas.model.PodMetrics;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.dao.InfraMapper;
import com.mobaas.paas.service.MonitoringService;
import com.mobaas.paas.service.NotifierService;

@Service
public class MonitoringServiceImpl implements MonitoringService {

	@Autowired
	private InfraMapper infraMapper;
	@Autowired
	private NotifierService notifierService;
	@Autowired
	private PaasConfig config;
	
	private Map<String, PodMetricsQueue> podMetricsMap = new HashMap<>();
	private Map<String, NodeMetricsQueue> nodeMetricsMap = new HashMap<>();
	
	@Override
	public void handlePodMetrics(PodMetrics metrics) {
		
		if (!podMetricsMap.containsKey(metrics.getPodName()) ) {
			DockerInfo dkinf = infraMapper.selectDockerInfoByAppId(metrics.getAppId());
			if (dkinf==null)
				return;
			podMetricsMap.put(metrics.getPodName(), new PodMetricsQueue(metrics.getAppId(), dkinf));
		}
		
		podMetricsMap.get(metrics.getPodName()).handle(metrics);
	}

	@Override
	public void handleNodeMetrics(NodeMetrics metrics) {
		if (!nodeMetricsMap.containsKey(metrics.getNodeName()) ) {
			Host host = infraMapper.selectHostByName(metrics.getNodeName());
			nodeMetricsMap.put(metrics.getNodeName(), new NodeMetricsQueue(host));
		}
		
		nodeMetricsMap.get(metrics.getNodeName()).handle(metrics);
	}

	private class PodMetricsQueue {

		private int overloadingCount;
		private String appId;
		private DockerInfo  dockerInfo;
		private Queue<PodMetrics> metricsQueue;
		
		public PodMetricsQueue(String appId, DockerInfo dkinf) {
			this.appId = appId;
			this.dockerInfo = dkinf;
		}
		
		public void handle(PodMetrics metrics) {
			if (metricsQueue == null) {
				metricsQueue = new LinkedList<>();
			}
			metricsQueue.offer(metrics);
			
			if (metricsQueue.size() < 10)
				return;

			if (metricsQueue.size() > 10) {
				metricsQueue.poll();
			}
			
			double memUsage = metricsQueue.stream().collect(Collectors.averagingLong(mem->mem.getMemoryUsage()));
			double percent = memUsage / (this.dockerInfo.getMemory() * 1024);
			if (percent > config.getAlertingPodMemoryOverload()) {  // overloading
				++overloadingCount;
				if (overloadingCount==1) {
					notifierService.notify(appId, appId, appId + " memory use " + Math.round(percent * 100) + "%");
				}
				if (overloadingCount>5) {
					overloadingCount=0;
				}
			} else {
				if (overloadingCount>0) {
					if (percent < config.getAlertingPodMemoryNormal()) {
						overloadingCount = 0;  //resume normal.
						notifierService.notify(appId, appId, appId + " memory use " + Math.round(percent * 100) + "%");
					}
				}
			}
		}
	}
	
	private class NodeMetricsQueue {
		
		private int overloadingCount;
		private Host host;
		private Queue<NodeMetrics> metricsQueue;
		
		public NodeMetricsQueue(Host host) {
			this.host = host;
		}
		
		public void handle(NodeMetrics metrics) {
			if (metricsQueue == null) {
				metricsQueue = new LinkedList<>();
			}
			metricsQueue.offer(metrics);
			
			if (metricsQueue.size() < 10)
				return;

			if (metricsQueue.size() > 10) {
				metricsQueue.poll();
			}
			
			double memUsage = metricsQueue.stream().collect(Collectors.averagingLong(mem->mem.getMemoryUsage()));
			double percent = memUsage / (host.getMemory() * 1024);
			if (percent > config.getAlertingNodeMemoryOverload()) {
				++overloadingCount;
				if (overloadingCount==1) {
					notifierService.notify(host.getName(), host.getName(), host.getName() + " memory use " + Math.round(percent * 100) + "%");
				}
				if (overloadingCount>5) {
					overloadingCount=0;
				}
			} else {
				if (overloadingCount > 0) {  // resume normal
					if (percent < config.getAlertingNodeMemoryNormal()) {
						overloadingCount = 0;
						notifierService.notify(host.getName(), host.getName(), host.getName() + " memory use " + Math.round(percent * 100) + "%");
					}
				}
			}
		}
	}
}
