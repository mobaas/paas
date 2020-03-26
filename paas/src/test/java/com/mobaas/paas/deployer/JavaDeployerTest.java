package com.mobaas.paas.deployer;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.deploy.Deployer;
import com.mobaas.paas.deploy.DeployerFactory;
import com.mobaas.paas.service.AppService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaDeployerTest {

	@Autowired
	private AppService appService;
	@Autowired
	private DeployerFactory deployerFactory;
	@Autowired
	private ApplicationContext context;
	
	@Test
	public void testHandleFile() throws IOException {
		
		AppInfo appInfo = appService.selectAppInfoById("kfayun-console");
		
		Deployer deployer = deployerFactory.createDeployer(appInfo.getPlatform());
		context.getAutowireCapableBeanFactory().autowireBean(deployer);

		AppVersion ver = appService.selectAppVersionByVersion(appInfo.getAppId(), "0.1.6");
		deployer.handleFile(appInfo, ver);
	}
}
