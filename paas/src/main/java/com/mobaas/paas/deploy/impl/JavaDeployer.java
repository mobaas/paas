/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.deploy.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.kubernetes.DeploymentVolume;

public class JavaDeployer extends BaseDeployer {

	private static final Logger log = LoggerFactory.getLogger(JavaDeployer.class);
	
	private static final int CONTAINER_PORT = 8080;
	private static final int MERTICS_PORT = 8090;
	
	@Override
	protected int getContainerPort() {
		return CONTAINER_PORT;
	}
	
	@Override
	protected String getContainerImage() {
		return "registry-internal.cn-qingdao.aliyuncs.com/mobaas/java:8";
	}

	@Override
	protected String getDeploymentTemplate() {
		return "java-deployment.yaml.ftl";
	}

	@Override
	protected String getServiceTemplate() {
		return "java-service.yaml.ftl";
	}
	
	@Override
	public void handleFile(AppInfo app, AppVersion ver) throws IOException {
		if (!ver.getFilename().endsWith(".jar")) {
			return;
		}
		
		Path verPath = Paths.get(config.getAppsRoot(), app.getAppId(), ver.getVersion());
		if (!Files.exists(verPath)) {
			Files.createDirectories(verPath);
		}
		
		Path targetFile = Paths.get(verPath.toString(), ver.getFilename());
		if (!Files.exists(targetFile)) {
			log.info(String.format("down file %s -> %s", ver.getFilePath(), targetFile.toAbsolutePath()));
			Path sourceFile = Paths.get(config.getUploadPath(), ver.getFilePath());
			Files.copy(sourceFile, targetFile);
		}
		
		if (!StringUtils.isEmpty(ver.getDataPath())) {
			Path uploadPath = Paths.get(config.getAppsRoot(), app.getAppId(), "upload");
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			Path uploadFile = Paths.get(uploadPath.toString(), ver.getDataName());

			log.info(String.format("down file %s -> %s", ver.getDataPath(), uploadFile.toAbsolutePath()));
			Path sourceFile = Paths.get(config.getUploadPath(), ver.getFilePath());
			try (InputStream inStream = new FileInputStream(sourceFile.toFile()) ) {
				unzip(inStream, uploadPath);
			} catch (ArchiveException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	protected void handlePaths(AppInfo appInfo, AppVersion ver, List<DeploymentVolume> volumeList) {

		boolean customUpload = false;
		for (DeploymentVolume volume : volumeList) {
			if ("upload".equals(volume.getName())) {
				customUpload = true;
				break;
			}
		}

		volumeList.add(new DeploymentVolume("home", String.format("%s/%s/%s", config.getAppsRoot(), appInfo.getAppId(), ver.getVersion()), "/home", null));
		if (!customUpload) {
			volumeList.add(new DeploymentVolume("upload", String.format("%s/%s/upload", config.getAppsRoot(), appInfo.getAppId()), "/home/upload/", "kfayun.upload-path"));
		}
		
	}

	@Override
	protected Map<String, Object> getCustomEnvironments(AppInfo appInfo, AppVersion ver) {

		Map<String, Object> envMap = new HashMap<>();
		envMap.put("JAVA_OPTS", "-Dserver.port=" + CONTAINER_PORT + " -Dmanagement.server.port=" + MERTICS_PORT);
        envMap.put("JAR_FILE", "/home/" + ver.getFilename());
       
        return envMap;
	}
	
	@Override
	protected Map<String, Object> getDeploymentModel(String deployName, String appName, String appVersion, String namespace, int instanceNum) {
		
        Map<String, Object> dataModel = new HashMap<>();
        //向数据集中添加数据
        dataModel.put("deployeeName", deployName);
        dataModel.put("appName", appName);
        dataModel.put("namespace", namespace);
        dataModel.put("appVersion", appVersion);
        dataModel.put("instanceNum", instanceNum);
        dataModel.put("containerImage", getContainerImage());
        dataModel.put("containerPort", getContainerPort());
        dataModel.put("metricsPort", MERTICS_PORT);
                       
        return dataModel;
	}

	@Override
	protected Map<String, Object> getServiceModel(AppInfo appInfo)  {
		
        Map<String, Object> dataModel = new HashMap<>();
        //向数据集中添加数据
        dataModel.put("appName", appInfo.getAppId());
        dataModel.put("svcName", StringUtils.isEmpty(appInfo.getServiceId()) ? appInfo.getAppId() : appInfo.getServiceId());
        dataModel.put("namespace", appInfo.getNamespace());
        dataModel.put("containerPort", getContainerPort());
        dataModel.put("metricsPort", MERTICS_PORT);
        
        return dataModel;
	}

}
