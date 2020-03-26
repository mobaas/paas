/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import java.io.IOException;

import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;

import io.kubernetes.client.ApiException;

public interface InstanceService {

	void start(AppInfo appInfo, AppVersion appVer);

	void stop(AppInfo appInfo);
	
	void deploy(AppInfo appInfo, AppVersion appVer);
	
	void changeNum(AppInfo appInfo, AppVersion appVer) throws IOException, ApiException;
}
