package com.mobaas.paas.config;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mobaas.paas.ComponentFactory;
import com.mobaas.paas.Notifier;

@Configuration
@ConfigurationProperties(prefix = "paas.components")
public class NotifierConfig {
	
	private Map<String, String> notifiers;

	@Autowired
	private ApplicationContext context;
	
	public void setNotifiers(Map<String, String> notifiers) {
		this.notifiers = notifiers;
	}
	
	@Bean
	public ComponentFactory<Notifier> notifierFactory() {
		
		ComponentFactory<Notifier> factory = new ComponentFactory<>(context);
		
		for (Entry<String, String> entry : notifiers.entrySet()) {
			try {
				Class<? extends Notifier> clazz = (Class<? extends Notifier>) Class.forName(entry.getValue());
				factory.register(entry.getKey(), clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return factory;
	}
	
}
