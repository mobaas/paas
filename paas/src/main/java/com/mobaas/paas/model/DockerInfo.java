/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DockerInfo {
	
	private int id;
	private String dockerNo;
	private int cpuNum;
	private int cpuPercent;
	private int memory;
	private int sysDisk;
	private BigDecimal monthPrice;
	
	public int getSysDisk() {
		return sysDisk;
	}
	public void setSysDisk(int sysDisk) {
		this.sysDisk = sysDisk;
	}
	public int getCpuNum() {
		return cpuNum;
	}
	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDockerNo() {
		return dockerNo;
	}
	public void setDockerNo(String dockerNo) {
		this.dockerNo = dockerNo;
	}
	public int getCpuPercent() {
		return cpuPercent;
	}
	public void setCpuPercent(int cpuPercent) {
		this.cpuPercent = cpuPercent;
	}
	public BigDecimal getMonthPrice() {
		return monthPrice;
	}
	public void setMonthPrice(BigDecimal monthPrice) {
		this.monthPrice = monthPrice;
	}
	
	@JsonIgnore
	public String getDescription() {
		DecimalFormat df = new DecimalFormat("#.##");
		return String.format("CPU x%d, 内存：%d MB，月价格：¥%s", cpuNum, memory, df.format(monthPrice));
	}

}
