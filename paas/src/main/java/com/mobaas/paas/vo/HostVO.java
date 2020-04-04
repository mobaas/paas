package com.mobaas.paas.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mobaas.paas.model.Host;

public class HostVO {

	private Host host;
	
	public HostVO(Host host) {
		this.host = host;
	}
	
	public int getId() {
		return host.getId();
	}
	
	public String getHostIp() {
		return host.getHostIp();
	}
	
	public String getName() {
		return host.getName();
	}
	
	public int getGroupId() {
		return host.getGroupId();
	}
	
	public String getOuterIp() {
		return host.getOuterIp();
	}
	
	public String getInstanceId() {
		return host.getInstanceId();
	}
	
	public String getProvider() {
		return host.getProvider();
	}
	
	public int getCpuNum() {
		return host.getCpuNum();
	}
	
	public int getMemory() {
		return host.getMemory();
	}
	
	public int getState() {
		return host.getState();
	}
	
	public Date getAddTime() {
		return host.getAddTime();
	}
	
	public int getInstanceNum() {
		return host.getInstanceNum();
	}
	
	public long getCpuPercent() {
		if (host.getMetricsList() == null)
			return 0;
		
		return Math.round( host.getMetricsList().stream()
				.collect(Collectors.averagingInt(metrics->metrics.getCpuUsage())) * 100f / (host.getCpuNum() * 1000000000) );
	}
	
	public long getMemoryPercent() {
		if (host.getMetricsList() == null)
			return 0;
		
		return Math.round( host.getMetricsList().stream()
				.collect(Collectors.averagingInt(metrics->metrics.getMemoryUsage())) * 100f / (host.getMemory() * 1024) );
	}
	
}
