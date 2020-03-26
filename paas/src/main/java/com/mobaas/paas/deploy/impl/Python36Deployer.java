/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.deploy.impl;

import java.io.IOException;
import java.util.List;

import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.kubernetes.DeploymentVolume;

public class Python36Deployer extends BaseDeployer {

	@Override
	protected String getContainerImage() {
		return "registry-internal.cn-qingdao.aliyuncs.com/mobaas/python:3.6";
	}

	@Override
	public void handleFile(AppInfo app, AppVersion ver) throws IOException {
		
	}

	@Override
	protected void handlePaths(AppInfo appInfo, AppVersion ver, List<DeploymentVolume> volumeList) {
		
		volumeList.add(new DeploymentVolume("workspace", String.format("%s/%s", config.getAppsRoot(), appInfo.getAppId()), "/home/workspace", null));
		
	}
	
}
