/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AppInfo {

	private int id;
	private String appId;
	private String serviceId;
	private String appSecret;
	private int userId;
	private String name;
	private String namespace;
	private int groupId;
	 private Date addTime;
	
	 private String domain; // 域名
	 private String platform; //
	 private String imageName; //
	 private String appVersion;  //
	 
	 private int enableTls;
	 
     private int instanceNum; // 实例数量
     private String dockerNo;
	 
     private String environments;
     private String volumes;
     
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



	public String getServiceId() {
		return serviceId;
	}



	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}



	public String getAppSecret() {
		return appSecret;
	}



	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}



	public int getUserId() {
		return userId;
	}



	public void setUserId(int userId) {
		this.userId = userId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getNamespace() {
		return namespace;
	}



	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}



	public int getGroupId() {
		return groupId;
	}



	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}



	public Date getAddTime() {
		return addTime;
	}


	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getDomain() {
		return domain;
	}



	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPlatform() {
		return platform;
	}



	public void setPlatform(String platform) {
		this.platform = platform;
	}



	public String getImageName() {
		return imageName;
	}



	public void setImageName(String imageName) {
		this.imageName = imageName;
	}



	public String getAppVersion() {
		return appVersion;
	}



	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public int getEnableTls() {
		return enableTls;
	}



	public void setEnableTls(int enableTls) {
		this.enableTls = enableTls;
	}

	public int getInstanceNum() {
		return instanceNum;
	}



	public void setInstanceNum(int instanceNum) {
		this.instanceNum = instanceNum;
	}



	public String getDockerNo() {
		return dockerNo;
	}

	public void setDockerNo(String dockerNo) {
		this.dockerNo = dockerNo;
	}

	public String getEnvironments() {
		return environments;
	}



	public void setEnvironments(String environments) {
		this.environments = environments;
	}

	public String getVolumes() {
		return volumes;
	}

	public void setVolumes(String volumes) {
		this.volumes = volumes;
	}

}
