/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.deploy.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.kubernetes.DeploymentVolume;

public class Tomcat8Deployer extends BaseDeployer {

	private static final Logger log = LoggerFactory.getLogger(Tomcat8Deployer.class);
	
	private static final int CONTAINER_PORT = 8080;
	
	@Override
	protected int getContainerPort() {
		return CONTAINER_PORT;
	}
	
	@Override
	protected String getContainerImage() {
		return "registry-internal.cn-qingdao.aliyuncs.com/mobaas/tomcat:8";
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

		volumeList.add(new DeploymentVolume("home", String.format("%s/%s/%s/www", config.getAppsRoot(), appInfo.getAppId(), ver.getVersion()), "/home/www", null));
		if (!customUpload) {
			volumeList.add(new DeploymentVolume("upload", String.format("%s/%s/upload", config.getAppsRoot(), appInfo.getAppId()), "/home/www/upload/", "kfayun.upload-path"));
		}
		
	}
	
	@Override
	public void handleFile(AppInfo appInfo, AppVersion ver) throws IOException {
		if (!ver.getFilename().endsWith(".war")) {
			return;
		}
		
		Path target = Paths.get(config.getAppsRoot(), appInfo.getAppId(), ver.getVersion(), "www");
		if (!Files.exists(target)) {
			Files.createDirectories(target);
		}
		
		log.info(String.format("down file %s -> %s", ver.getFilePath(), target.toAbsolutePath()));
		Path sourceFile = Paths.get(config.getUploadPath(), ver.getFilePath());
		try (InputStream inStream = new FileInputStream(sourceFile.toFile())) {
			
			unpack(inStream, target);
			
			// abc/1.0.0/www/upload  -> abc/upload
			Path upload = Paths.get(target.toString(), "upload/");
			Path to = Paths.get(config.getAppsRoot(), appInfo.getAppId(), "upload/");
			if (!Files.exists(to)) {
				Files.createDirectory(to);
			}
			FileUtils.copyDirectory(upload.toFile(), to.toFile());
		} catch (ArchiveException ex) {
			throw new IOException(ex);
		}
	}
	
	private void unpack(InputStream inStream, Path target) throws IOException, ArchiveException {
		 BufferedInputStream bis = new BufferedInputStream(inStream);
	        
	    	ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, bis);
	    	 
        JarArchiveEntry jarEntry = null;
        while ((jarEntry = (JarArchiveEntry) in.getNextEntry()) != null) {
        		try {
	       	 	if (jarEntry.isDirectory()) {
	         	 	Path path = target.resolve(jarEntry.getName());
	         	 	if (!Files.exists(path)) {
	         	 		Files.createDirectories(path);
	         	 	}
	             } else {
	            	 	Path path = target.resolve(jarEntry.getName());
	                Files.copy(in, path); 
	             }
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
        }
	}
	
}
