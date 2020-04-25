package com.mobaas.paas.model;

import java.util.Date;

public class AccessTotal {

	private Date time;
	private String service;
	private int total;
	private int totalIp;
	private int total20x;
	private int total30x;
	private int total40x;
	private int total50x;
	private float avgRequestTime;
	private int totalRequestBytes;
	private int totalResponseBytes;
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getTotalIp() {
		return totalIp;
	}
	public void setTotalIp(int totalIp) {
		this.totalIp = totalIp;
	}
	public int getTotal20x() {
		return total20x;
	}
	public void setTotal20x(int total20x) {
		this.total20x = total20x;
	}
	public int getTotal30x() {
		return total30x;
	}
	public void setTotal30x(int total30x) {
		this.total30x = total30x;
	}
	public int getTotal40x() {
		return total40x;
	}
	public void setTotal40x(int total40x) {
		this.total40x = total40x;
	}
	public int getTotal50x() {
		return total50x;
	}
	public void setTotal50x(int total50x) {
		this.total50x = total50x;
	}
	public float getAvgRequestTime() {
		return avgRequestTime;
	}
	public void setAvgRequestTime(float avgRequestTime) {
		this.avgRequestTime = avgRequestTime;
	}
	public int getTotalRequestBytes() {
		return totalRequestBytes;
	}
	public void setTotalRequestBytes(int totalRequestBytes) {
		this.totalRequestBytes = totalRequestBytes;
	}
	public int getTotalResponseBytes() {
		return totalResponseBytes;
	}
	public void setTotalResponseBytes(int totalResponseBytes) {
		this.totalResponseBytes = totalResponseBytes;
	}
	
}
