/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.vo;

import java.util.Date;
import java.util.stream.Collectors;

import com.mobaas.paas.model.DockerInfo;
import com.mobaas.paas.model.AppInstance;

public class AppInstanceVO {

	private AppInstance inst;
	private DockerInfo product;
	
	public AppInstanceVO(AppInstance inst, DockerInfo product) {
		this.inst = inst;
		this.product = product;
	}
	
	public String getPodName() { 
		return inst.getPodName(); 
		}
	
	public String getAppName() { 
		return inst.getAppName(); 
		}
	
	public String getNamespace() { 
		return inst.getNamespace(); 
		}
	
	public String getAppVersion() { 
		return inst.getAppVersion(); 
		}
	
	public String getNodeName() { 
		return inst.getNodeName(); 
		}
	
	public String getHostIP() { 
		return inst.getHostIP(); 
		}
	
	public String getStatus() { 
		return inst.getStatus(); 
		}
	
	public Date getStartTime() { 
		return inst.getStartTime(); 
		}
	
	public long getCpuPercent() {
		if (inst.getMetricsList() == null || product == null)
			return 0;
		
		double average = inst.getMetricsList().stream()
				.collect(Collectors.averagingInt(metrics->metrics.getCpuUsage()));
		return Math.round( average * 100f / (product.getCpuNum() * product.getCpuPercent() * 1000000) );
	}
	
	public long getMemoryPercent() {
		if (inst.getMetricsList() == null || product == null)
			return 0;
		
		double average = inst.getMetricsList().stream()
				.collect(Collectors.averagingLong(mem->mem.getMemoryUsage()));
		return Math.round( average * 100f / (product.getMemory() * 1024) );
	}
}
