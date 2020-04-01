package com.mobaas.paas.model;

import java.util.Date;

/*
 * 应用灰度版本
 */
public class AppGrayVersion {
	
	private int id;
	private String appId;
	private String version;
	private int instanceNum;
	private Date addTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getInstanceNum() {
		return instanceNum;
	}
	public void setInstanceNum(int instanceNum) {
		this.instanceNum = instanceNum;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
}
