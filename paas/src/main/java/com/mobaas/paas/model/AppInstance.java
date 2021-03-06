/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.model;

import java.util.Date;
import java.util.List;

public class AppInstance {

	private String podName;  // metadata/name
	private String appName;  // metadata/labels/k8s-app
	private String namespace; // metadata/namespace
	private String appVersion; // metadata/labels/version
	private String nodeName; // spec/nodeName
	private String hostIP;  // status/hostIP
	private String status;  // status/phase
	private int ready;
	private Date startTime; // status/startTime
	private List<PodMetrics> metricsList;
	
	public String getPodName() {
		return podName;
	}
	public void setPodName(String podName) {
		this.podName = podName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getHostIP() {
		return hostIP;
	}
	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getReady() {
		return ready;
	}
	public void setReady(int ready) {
		this.ready = ready;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public List<PodMetrics> getMetricsList() {
		return metricsList;
	}
	public void setMetricsList(List<PodMetrics> metricsList) {
		this.metricsList = metricsList;
	}
	
}
