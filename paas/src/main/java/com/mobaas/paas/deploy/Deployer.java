/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.deploy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.mobaas.paas.model.AppGrayVersion;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.kubernetes.DeploymentVolume;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Service;

public interface Deployer {
	
	void handleFile(AppInfo app, AppVersion ver) throws IOException;
	
	void deploy(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws ApiException, IOException ;

	void grayDeploy(AppInfo appInfo, AppVersion ver, AppGrayVersion gver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws ApiException, IOException ;

	void upgrade(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws ApiException ;
	
	void start(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws ApiException, IOException ;

	void delete(AppInfo appInfo) throws ApiException;

	void applyIngress(AppInfo appInfo) throws IOException, ApiException;
	
	void deleteIngress(AppInfo appInfo) throws IOException, ApiException;
	
	V1Deployment getDeployment(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws IOException;
	
	V1Deployment getGrayDeployment(AppInfo appInfo, AppVersion ver, AppGrayVersion grayVer, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws IOException;
	
	V1Service getService(AppInfo appInfo) throws IOException;
	
}
