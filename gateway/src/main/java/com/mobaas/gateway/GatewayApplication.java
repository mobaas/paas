/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.mobaas.gateway.config.CustomRibbonClientConfig;

@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableAspectJAutoProxy(proxyTargetClass = true)
@RibbonClients(value= {
		@RibbonClient(name="service1", configuration= CustomRibbonClientConfig.class)
})
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
