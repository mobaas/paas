/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.kubernetes;

public class DeploymentPatchItem {

	private String op;
	private String path;
	private Object value;
	
	public DeploymentPatchItem(String op, String path, Object value) {
		this.op = op;
		this.path = path;
		this.value = value;
	}
	
	public String getOp() { return op; }
	
	public String getPath() { return path; }
	
	public Object getValue() { return value; }
	
}
