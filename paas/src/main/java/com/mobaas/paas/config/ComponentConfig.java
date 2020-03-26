/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.config;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobaas.paas.AppContextUtil;
import com.mobaas.paas.kubernetes.KubeApiServiceImpl;
import com.mobaas.paas.service.KubeApiService;

@Configuration
public class ComponentConfig implements ApplicationContextAware {

	@Autowired
	private PaasConfig config;
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return om;
	}
	
	@Bean
	public KubeApiService kubeApiService() throws IOException {
		return new KubeApiServiceImpl(config);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AppContextUtil.setAppContext(applicationContext);
	}
}
