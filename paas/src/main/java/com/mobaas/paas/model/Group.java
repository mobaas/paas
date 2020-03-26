/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.model;

public class Group {
	
	private int id;
	private String name;
	private String region;
	private String summary;
	private String domainSuffix;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDomainSuffix() {
		return domainSuffix;
	}

	public void setDomainSuffix(String domainSuffix) {
		this.domainSuffix = domainSuffix;
	}
	
	
}
