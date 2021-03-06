/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobaas.paas.model.AppAction;
import com.mobaas.paas.model.AppGrayVersion;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.deploy.Deployer;
import com.mobaas.paas.deploy.DeployerFactory;
import com.mobaas.paas.kubernetes.DeploymentVolume;
import com.mobaas.paas.service.AppService;
import com.mobaas.paas.service.InstanceService;
import com.mobaas.paas.service.KubeApiService;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;

@Service
public class InstanceServiceImpl implements InstanceService {

	@Autowired
	private AppService appService;
	@Autowired
    private ObjectMapper jsonMapper;
	@Autowired
	private KubeApiService kubeService;
	@Autowired
    private ApplicationContext appContext;
	@Autowired
	private DeployerFactory deployerFactory;
	@Autowired
	private PaasConfig config;
	
	private static ExecutorService es = Executors.newCachedThreadPool(); 

	@Override
	public void start(AppInfo appInfo, AppVersion appVer) {

		AppAction action = new AppAction();
		action.setAppId(appInfo.getAppId());
		action.setVersion(appVer.getVersion());
		action.setAction("start");
		action.setAddTime(new Date());
		appService.insertAppAction(action);
		
		es.execute(new StartRunnable(appInfo, appVer, action));
	}

	@Override
	public void stop(AppInfo appInfo) {

		AppAction action = new AppAction();
		action.setAppId(appInfo.getAppId());
		action.setVersion("");
		action.setAction("stop");
		action.setAddTime(new Date());
		appService.insertAppAction(action);
		
		es.execute(new StopRunnable(appInfo, action));
	}
	
	@Override
	public void deploy(AppInfo appInfo, AppVersion appVer) {
		
		AppAction action = new AppAction();
		action.setAppId(appInfo.getAppId());
		action.setVersion(appVer.getVersion());
		action.setAction("deploy");
		action.setAddTime(new Date());
		appService.insertAppAction(action);
		
		es.execute(new DeployRunnable(appInfo, appVer, action));
	}
	
	@Override
	public void grayDeploy(AppInfo appInfo, AppVersion appVer, AppGrayVersion grayVer) {
		
		AppAction action = new AppAction();
		action.setAppId(appInfo.getAppId());
		action.setVersion(appVer.getVersion());
		action.setAction("graydeploy");
		action.setAddTime(new Date());
		appService.insertAppAction(action);
		
		es.execute(new GrayDeployRunnable(appInfo, appVer, grayVer, action));
	}
	
	@Override
	public void grayRelease(AppInfo appInfo, AppGrayVersion grayVer) throws ApiException {
		
		AppAction action = new AppAction();
		action.setAppId(appInfo.getAppId());
		action.setVersion(grayVer.getVersion());
		action.setAction("grayrelease");
		action.setAddTime(new Date());
		appService.insertAppAction(action);
		
		String deployName = appInfo.getAppId() + "-" + grayVer.getVersion();
		V1Deployment deploy = kubeService.queryDeployment(deployName, appInfo.getNamespace());
	
		if (deploy != null) {
			kubeService.deleteDeployment(deployName, appInfo.getNamespace(), null);
		}
	}
	
	@Override
	public void changeNum(AppInfo appInfo, AppVersion appVer) throws IOException, ApiException {
		
		Map<String, Object> envMap = new HashMap<>();
    		List<DeploymentVolume> volumeList = new ArrayList<>();
    		getDeploymentData(appInfo, appVer, envMap, volumeList);
		getDeployer(appInfo.getPlatform()).deploy(appInfo, appVer, envMap, volumeList);
	}

	@Override
	public Map<String, Integer> queryInstanceNumForNode() {

		Map<String, Integer> map = new HashMap<>();
		try {
			List<V1Pod> list = new ArrayList<>();
	
			String[] excludes = config.getExcludeNamespaces().split(",");
			V1NamespaceList nslist = kubeService.queryNamespaceList();
			for (V1Namespace ns : nslist.getItems()) {
				
				boolean ignore = false;
				for (String prefix : excludes) {
					if (ns.getMetadata().getName().startsWith(prefix)) {
						ignore = true;
						break;
					}
				}
				
				if (!ignore) {
					V1PodList plist = kubeService.queryPodList(null, ns.getMetadata().getName());
					list.addAll(plist.getItems());
					
				}
			}
			
			for (V1Pod pod : list) {
				String hostIP = pod.getStatus().getHostIP();
				if (map.containsKey(hostIP)) {
					map.put(hostIP, map.get(hostIP)+1);
				} else {
					map.put(hostIP, 1);
				}
			}
		} catch (ApiException ex) {
			ex.printStackTrace();
		}
		return map;
	}

	@Override
	public int queryInstanceTotal() {
		int total = 0;
		try {
			String[] excludes = config.getExcludeNamespaces().split(",");
			V1NamespaceList nslist = kubeService.queryNamespaceList();
			for (V1Namespace ns : nslist.getItems()) {
				
				boolean ignore = false;
				for (String prefix : excludes) {
					if (ns.getMetadata().getName().startsWith(prefix)) {
						ignore = true;
						break;
					}
				}
				
				if (!ignore) {
					V1PodList plist = kubeService.queryPodList(null, ns.getMetadata().getName());
					total += plist.getItems().size();
					
				}
			}
		} catch (ApiException ex) {
			ex.printStackTrace();
		}
		
		return total;
	}
	
	private Deployer getDeployer(String platform) {
		Deployer deployer = deployerFactory.createDeployer(platform);
		appContext.getAutowireCapableBeanFactory().autowireBean(deployer);
		return deployer;
	}

	private void getDeploymentData(AppInfo appInfo, AppVersion ver, 
			Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws IOException {
		envMap.put("TZ", "Asia/Shanghai");
	
		if (!StringUtils.isEmpty(appInfo.getEnvironments())) {
	    		Map<String, Object> map = (Map<String, Object>)jsonMapper.readValue(appInfo.getEnvironments(), Map.class);
	    		envMap.putAll(map);
		}
	
		if (!StringUtils.isEmpty(appInfo.getVolumes())) {
			DeploymentVolume[] volumes = jsonMapper.readValue(appInfo.getVolumes(), DeploymentVolume[].class);
			for (int i=0; i<volumes.length; i++) {
				volumeList.add(volumes[i]);
			}
		}
		
	}

	private class StartRunnable implements Runnable {

		private AppInfo appInfo;
		private AppVersion version;
		private AppAction action;
		
		public StartRunnable(AppInfo appInfo, AppVersion version, AppAction action) {
			this.appInfo = appInfo;
			this.version = version;
			this.action = action;
		}
		
		@Override
		public void run() {

			try {
				V1Deployment deploy = kubeService.queryDeployment(appInfo.getAppId(), appInfo.getNamespace());
				if (deploy != null) {
					kubeService.deleteDeployment(appInfo.getAppId(), appInfo.getNamespace(), null);
				}
				
				Deployer deployer = getDeployer(appInfo.getPlatform());
				
				Map<String, Object> envMap = new HashMap<>();
				List<DeploymentVolume> volumeList = new ArrayList<>();
				getDeploymentData(appInfo, version, envMap, volumeList);
				
				deployer.start(appInfo, version, envMap, volumeList);
				
				action.setState(1);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				action.setState(2);
				action.setResult(ex.getLocalizedMessage());
			}
			
			appService.updateAppAction(action);
		}
		
	}
	
	private class StopRunnable implements Runnable {

		private AppInfo appInfo;
		private AppAction action;
		
		public StopRunnable(AppInfo appInfo, AppAction action) {
			this.appInfo = appInfo;
			this.action = action;
		}
		
		@Override
		public void run() {

			try {
				V1Deployment deploy = kubeService.queryDeployment(appInfo.getAppId(), appInfo.getNamespace());
				if (deploy != null) {
					kubeService.deleteDeployment(appInfo.getAppId(), appInfo.getNamespace(), null);
				}
				
				action.setState(1);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				action.setState(2);
				action.setResult(ex.getLocalizedMessage());
			}
			
			appService.updateAppAction(action);
		}
		
	}
	
	private class DeployRunnable implements Runnable {

		private AppInfo appInfo;
		private AppVersion ver;
		private AppAction action;
		
		public DeployRunnable(AppInfo appInfo, AppVersion ver, AppAction action) {
			this.appInfo = appInfo;
			this.ver = ver;
			this.action = action;
		}
		
		@Override
		public void run() {

			try {
				Deployer deployer = getDeployer(appInfo.getPlatform());
				
				String lockVersion = (appInfo.getAppId() + "_" + appInfo.getAppVersion()).intern();
	
				synchronized(lockVersion) {
					
					deployer.handleFile(appInfo, ver);
	
				}
			
				Map<String, Object> envMap = new HashMap<>();
				List<DeploymentVolume> volumeList = new ArrayList<>();
				getDeploymentData(appInfo, ver, envMap, volumeList);
		
				V1Deployment deploy = kubeService.queryDeployment(appInfo.getAppId(), appInfo.getNamespace());
			
				if (deploy == null) {
					deployer.deploy(appInfo, ver, envMap, volumeList);
				} else {
		    			// 版本升级
		    			deployer.upgrade(appInfo, ver, envMap, volumeList);
				}
				
				action.setState(1);
			} catch (Exception ex) {
				ex.printStackTrace();
				//log.info(String.format("query deployment %s, error: %s", appInfo.getAppId(), ex.getLocalizedMessage()));		
				action.setState(2);
				action.setResult(ex.getLocalizedMessage());
			}
			
			appService.updateAppAction(action);
		}
		
	}
	
	private class GrayDeployRunnable implements Runnable {

		private AppInfo appInfo;
		private AppVersion ver;
		private AppGrayVersion grayVer;
		private AppAction action;
		
		public GrayDeployRunnable(AppInfo appInfo, AppVersion ver, AppGrayVersion grayVer, AppAction action) {
			this.appInfo = appInfo;
			this.ver = ver;
			this.grayVer = grayVer;
			this.action = action;
		}
		
		@Override
		public void run() {

			try {
				Deployer deployer = getDeployer(appInfo.getPlatform());
				
				String lockVersion = (appInfo.getAppId() + "_" + ver.getVersion()).intern();
	
				synchronized(lockVersion) {
					deployer.handleFile(appInfo, ver);
				}
			
				Map<String, Object> envMap = new HashMap<>();
				List<DeploymentVolume> volumeList = new ArrayList<>();
				getDeploymentData(appInfo, ver, envMap, volumeList);
		
				String deployName = appInfo.getAppId() + "-" + ver.getVersion();
				V1Deployment deploy = kubeService.queryDeployment(deployName, appInfo.getNamespace());
			
				if (deploy != null) {
					kubeService.deleteDeployment(deployName, appInfo.getNamespace(), null);
				}
				deployer.grayDeploy(appInfo, ver, grayVer, envMap, volumeList);
				
				action.setState(1);
			} catch (Exception ex) {
				ex.printStackTrace();
				//log.info(String.format("query deployment %s, error: %s", appInfo.getAppId(), ex.getLocalizedMessage()));		
				action.setState(2);
				action.setResult(ex.getLocalizedMessage());
			}
			
			appService.updateAppAction(action);
		}
		
	}
}
