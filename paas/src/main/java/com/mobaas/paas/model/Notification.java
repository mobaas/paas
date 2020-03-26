package com.mobaas.paas.model;

/**
 * 
 * @author billy zhang
 * 
 */
public class Notification {

	private int id;
	private int kind;
	private int userId;
	private String notifier;
	private String config;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
