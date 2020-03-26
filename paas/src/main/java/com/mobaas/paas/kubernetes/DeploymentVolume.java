/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.kubernetes;

public class DeploymentVolume {

	private String name;
	private String hostPath;
	private String mountPath;
	private String envName;
	
	public DeploymentVolume() {
		
	}
	
	public DeploymentVolume(String name, String hostPath, String mountPath, String envName) {
		this.name = name;
		this.hostPath = hostPath;
		this.mountPath = mountPath;
		this.envName = envName;
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getHostPath() { return hostPath; }
	public void setHostPath(String hostPath) { this.hostPath = hostPath; }
	
	public String getMountPath() { return mountPath; }
	public void setMountPath(String mountPath) { this.mountPath = mountPath; }
	
	public String getEnvName() { return envName; }
	public void setEnvName(String envName) { this.envName = envName; }
}
