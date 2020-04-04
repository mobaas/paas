/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author billy zhang
 * 
 */
@Component
public class PaasConfig {

    @Value("${paas.upload-path}")
    private String uploadPath;
    
    @Value("${paas.apps-root}")
    private String appsRoot;
    
    @Value("${paas.kube-config}")
    private String kubeConfig;
    
    @Value("${paas.exclude-namespaces}")
    private String excludeNamespaces;
    
    @Value("${paas.alerting.pod-memory-overload}")
    private float alertingPodMemoryOverload;

    @Value("${paas.alerting.pod-memory-normal}")
    private float alertingPodMemoryNormal;

    @Value("${paas.alerting.pod-cpu-overload}")
    private float alertingPodCpuOverload;

    @Value("${paas.alerting.pod-cpu-normal}")
    private float alertingPodCpuNormal;
    
    @Value("${paas.alerting.node-memory-overload}")
    private float alertingNodeMemoryOverload;

    @Value("${paas.alerting.node-memory-normal}")
    private float alertingNodeMemoryNormal;

    @Value("${paas.alerting.node-cpu-overload}")
    private float alertingNodeCpuOverload;

    @Value("${paas.alerting.node-cpu-normal}")
    private float alertingNodeCpuNormal;
    
	public String getUploadPath() {
		return uploadPath;
	}
	
	public String getAppsRoot() {
		return appsRoot;
	}
	
	public String getKubeConfig() {
		return kubeConfig;
	}
	
	public String getExcludeNamespaces() {
		return excludeNamespaces;
	}
	
	public float getAlertingPodMemoryOverload() {
		return alertingPodMemoryOverload;
	}
	    
	public float getAlertingPodMemoryNormal() {
		return alertingPodMemoryNormal;
	}
	    
	public float getAlertingPodCpuOverload() {
		return alertingPodCpuOverload;
	}
	    
	public float getAlertingPodCpuNormal() {
		return alertingPodCpuNormal;
	}
	    
	public float getAlertingNodeMemoryOverload() {
		return alertingNodeMemoryOverload;
	}
	    
	public float getAlertingNodeMemoryNormal() {
		return alertingNodeMemoryNormal;
	}
	    
	public float getAlertingNodeCpuOverload() {
		return alertingNodeCpuOverload;
	}
	    
	public float getAlertingNodeCpuNormal() {
		return alertingNodeCpuNormal;
	}
}
