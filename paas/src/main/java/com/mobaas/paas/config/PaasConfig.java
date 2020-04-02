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

	@Value("${paas.schedule.dispatch-cron}")
    private String dispatchCron;
	
    @Value("${paas.upload-path}")
    private String uploadPath;
    
    @Value("${paas.apps-root}")
    private String appsRoot;
    
    @Value("${paas.kube-config}")
    private String kubeConfig;
    
    @Value("${paas.exclude-namespaces}")
    private String excludeNamespaces;
    
    @Value("${paas.metrics-keeping-days}")
    private int metricsKeepingDays;
    
    public String getDispatchCron() {
    	return dispatchCron;
    }
    
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
	
	public int getMetricsKeepingDays() {
		return metricsKeepingDays;
	}
	
}
