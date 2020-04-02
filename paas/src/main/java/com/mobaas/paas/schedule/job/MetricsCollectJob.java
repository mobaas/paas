package com.mobaas.paas.schedule.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.mobaas.kubernetes.models.MetricsV1beta1NodeMetrics;
import com.mobaas.kubernetes.models.MetricsV1beta1NodeMetricsList;
import com.mobaas.kubernetes.models.MetricsV1beta1PodContainer;
import com.mobaas.kubernetes.models.MetricsV1beta1PodMetrics;
import com.mobaas.kubernetes.models.MetricsV1beta1PodMetricsList;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.model.NodeMetrics;
import com.mobaas.paas.model.PodMetrics;
import com.mobaas.paas.service.KubeApiService;
import com.mobaas.paas.service.MetricsService;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;

public class MetricsCollectJob extends AbstractJob {
	
	@Autowired
	private PaasConfig config;
	@Autowired
	private MetricsService metricsService;
	@Autowired
	private KubeApiService kubeService;
	
	protected void doExecute(Map<String, Object> jobData) {
		
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_YEAR, config.getMetricsKeepingDays());
			
			metricsService.deletePodMetricsBeforeTime(cal.getTime());
			metricsService.deleteNodeMetricsBeforeTime(cal.getTime());
			
			List<MetricsV1beta1PodMetrics> list = queryPodMetricsList("");
			for (MetricsV1beta1PodMetrics metrics : list) {
				for (MetricsV1beta1PodContainer c : metrics.getContainers()) {
					PodMetrics usage = new PodMetrics();
					usage.setAppId(c.getName());
					usage.setPodName( metrics.getMetadata().getName() );
					usage.setTimestamp(metrics.getTimestamp().toDate());
					
					String cpuStr = c.getUsage().getCpu();
					if ( !StringUtils.isEmpty(cpuStr) ) {
						try {
							usage.setCpuUsage( Integer.parseInt(cpuStr.substring(0, cpuStr.length()-1)) );
						} catch (Exception ex) {};
					}
					
					String memoryStr = c.getUsage().getMemory();
					if ( !StringUtils.isEmpty(memoryStr)) {
						try {
							usage.setMemoryUsage( Integer.parseInt(memoryStr.substring(0, memoryStr.length()-2)) );
						} catch (Exception ex) {};
					}
					
					metricsService.insertPodMetrics(usage);
				}
			}
			
			MetricsV1beta1NodeMetricsList plist = kubeService.queryNodeMetricsList();
			for (MetricsV1beta1NodeMetrics metrics : plist.getItems()) {
				NodeMetrics usage = new NodeMetrics();
				usage.setNodeName(metrics.getMetadata().getName());
				usage.setTimestamp(metrics.getTimestamp().toDate());

				String cpuStr = metrics.getUsage().getCpu();
				if ( !StringUtils.isEmpty(cpuStr) ) {
				try {
					usage.setCpuUsage( Integer.parseInt(cpuStr.substring(0, cpuStr.length()-1)) );
				} catch (Exception ex) {};
			}
				
			String memoryStr = metrics.getUsage().getMemory();
			if ( !StringUtils.isEmpty(cpuStr) ) {
				try {
					usage.setMemoryUsage( Integer.parseInt(memoryStr.substring(0, memoryStr.length()-2)) );
				} catch (Exception ex) {};
			}
			
				metricsService.insertNodeMetrics(usage);
			}
    			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private List<MetricsV1beta1PodMetrics> queryPodMetricsList(String name) throws ApiException {
		List<MetricsV1beta1PodMetrics> list = new ArrayList<>();

		String[] excludes = config.getExcludeNamespaces().split(",");
		V1NamespaceList nslist = kubeService.queryNamespaceList();
		for (V1Namespace ns : nslist.getItems()) {
			
			boolean ignore = false;
			for (String prefix : excludes) {
				if (ns.getMetadata().getName().startsWith(prefix)) {
					ignore = true;
					break;
				}
			}
			
			if (!ignore) {
				MetricsV1beta1PodMetricsList plist = kubeService.queryPodMetricsList(ns.getMetadata().getName());
				if (name == null || name == "") {
					list.addAll(plist.getItems());
				} else {
					plist.getItems().forEach((pod)-> {
						if (pod.getMetadata().getName().contains(name)) {
							list.add(pod);
						}
					});
				}
			}
		}
		
		return list;
	}
	
}