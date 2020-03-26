/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.gateway.config;

import org.springframework.cloud.kubernetes.ribbon.KubernetesConfigKey;
import org.springframework.cloud.kubernetes.ribbon.KubernetesServerList;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;

import io.fabric8.kubernetes.client.KubernetesClient;

public class CustomRibbonClientConfig {

	@Bean
	public ServerList<?> ribbonServerList(KubernetesClient client, IClientConfig config) {
		KubernetesServerList serverList = new KubernetesServerList(client);
		 if ("service1".equalsIgnoreCase(config.getClientName()) ) {
        	config.set(KubernetesConfigKey.Namespace, "namespace1");  // 跨Namespace调用
        }
		serverList.initWithNiwsConfig(config);
		return serverList;
	}
	
}
