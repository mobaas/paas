/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.config;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobaas.paas.AppContextUtil;
import com.mobaas.paas.ComponentFactory;
import com.mobaas.paas.Notifier;
import com.mobaas.paas.kubernetes.KubeApiServiceImpl;
import com.mobaas.paas.service.KubeApiService;

@Configuration
@ConfigurationProperties(prefix = "paas.components")
public class ComponentConfig implements ApplicationContextAware {

	@Autowired
	private PaasConfig config;

	@Autowired
	private ApplicationContext context;
	
	private Map<String, String> notifiers;
	
	public void setNotifiers(Map<String, String> notifiers) {
		this.notifiers = notifiers;
	}

	@Bean
	public ComponentFactory<Notifier> notifierFactory() {
		
		return createComponentFactory(notifiers);
		
	}
	
	private <T> ComponentFactory<T> createComponentFactory(Map<String, String> map) {
		ComponentFactory<T> factory = new ComponentFactory<>(context);
		
		for (Entry<String, String> entry : map.entrySet()) {
			try {
				Class<? extends T> clazz = (Class<? extends T>) Class.forName(entry.getValue());
				factory.register(entry.getKey(), clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return factory;
	}
	
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
