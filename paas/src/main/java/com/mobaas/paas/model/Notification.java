/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.model;

/**
 * 
 * @author billy zhang
 * 
 */
public class Notification {

	private int id;
	private int name;
	private String target;
	private String notifier;
	private String config;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getName() {
		return name;
	}

	public String getTarget() {
		return target;
	}

	public void setName(int name) {
		this.name = name;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}

	public String getNotifier() {
		return notifier;
	}
	public void setNotifier(String notifier) {
		this.notifier = notifier;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	
	
}
