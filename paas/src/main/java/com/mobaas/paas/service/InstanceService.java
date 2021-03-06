/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import java.io.IOException;
import java.util.Map;

import com.mobaas.paas.model.AppGrayVersion;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;

import io.kubernetes.client.ApiException;

public interface InstanceService {

	void start(AppInfo appInfo, AppVersion appVer);

	void stop(AppInfo appInfo);
	
	void deploy(AppInfo appInfo, AppVersion appVer);
	
	void changeNum(AppInfo appInfo, AppVersion appVer) throws IOException, ApiException;
	
	void grayDeploy(AppInfo appInfo, AppVersion appVer, AppGrayVersion gver);

	void grayRelease(AppInfo appInfo, AppGrayVersion ver) throws ApiException;
	
	Map<String, Integer> queryInstanceNumForNode();
	
	int queryInstanceTotal();
}
