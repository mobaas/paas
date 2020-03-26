/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.deploy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mobaas.paas.deploy.impl.Java8Deployer;
import com.mobaas.paas.deploy.impl.JavaDeployer;
import com.mobaas.paas.deploy.impl.MonoDeployer;
import com.mobaas.paas.deploy.impl.Php56Deployer;
import com.mobaas.paas.deploy.impl.Python36Deployer;
import com.mobaas.paas.deploy.impl.Tomcat8Deployer;

@Component
public class DeployerFactory {

	private Map<String, Class<? extends Deployer>> deployerMap;
	
	public DeployerFactory() {
		
		deployerMap = new HashMap<>();
		deployerMap.put("java", JavaDeployer.class);
		deployerMap.put("java8", Java8Deployer.class);
		deployerMap.put("mono", MonoDeployer.class);
		deployerMap.put("tomcat8", Tomcat8Deployer.class);
		deployerMap.put("php56", Php56Deployer.class);
		deployerMap.put("python36", Python36Deployer.class);
	}
	

	public Deployer createDeployer(String platform) {
		
		if (deployerMap.containsKey(platform)) {
			Class<? extends Deployer> clazz = deployerMap.get(platform);
			
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
