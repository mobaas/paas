package com.mobaas.paas.model;

import java.util.Date;

public class AccessLog {

	private int id;
	private Date time;
	private String timeLocal;
	private String host;  // ngx.var.host
	private String namespace;  // ngx.var.namespace
	private String ingress;  // ngx.var.ingress_name
	private String service;  // ngx.var.service_name
	private String path;  // ngx.var.location_path
	private String method;  // ngx.var.request_method
	private String remoteAddr; 
	private int status;  // ngx.var.status
	private int requestLength;  // ngx.var.request_length
	private float requestTime;  // ngx.var.request_time
	private int responseLength;  // ngx.var.bytes_sent
	private float upstreamLatency;  // ngx.var.upstream_connect_time
	private float upstreamResponseTime;  // ngx.var.upstream_response_time
	private int upstreamResponseLength;  // ngx.var.upstream_response_length
	private int upstreamStatus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getTimeLocal() {
		return timeLocal;
	}
	public void setTimeLocal(String timeLocal) {
		this.timeLocal = timeLocal;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getIngress() {
		return ingress;
	}
	public void setIngress(String ingress) {
		this.ingress = ingress;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRequestLength() {
		return requestLength;
	}
	public void setRequestLength(int requestLength) {
		this.requestLength = requestLength;
	}
	public float getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(float requestTime) {
		this.requestTime = requestTime;
	}
	public int getResponseLength() {
		return responseLength;
	}
	public void setResponseLength(int responseLength) {
		this.responseLength = responseLength;
	}
	public float getUpstreamLatency() {
		return upstreamLatency;
	}
	public void setUpstreamLatency(float upstreamLatency) {
		this.upstreamLatency = upstreamLatency;
	}
	public float getUpstreamResponseTime() {
		return upstreamResponseTime;
	}
	public void setUpstreamResponseTime(float upstreamResponseTime) {
		this.upstreamResponseTime = upstreamResponseTime;
	}
	public int getUpstreamResponseLength() {
		return upstreamResponseLength;
	}
	public void setUpstreamResponseLength(int upstreamResponseLength) {
		this.upstreamResponseLength = upstreamResponseLength;
	}
	public int getUpstreamStatus() {
		return upstreamStatus;
	}
	public void setUpstreamStatus(int upstreamStatus) {
		this.upstreamStatus = upstreamStatus;
	}
	
	
}
