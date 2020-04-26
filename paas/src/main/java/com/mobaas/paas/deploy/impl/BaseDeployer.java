/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.deploy.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.mobaas.paas.model.AppGrayVersion;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.WebApplication;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.deploy.Deployer;
import com.mobaas.paas.kubernetes.DeploymentPatchItem;
import com.mobaas.paas.kubernetes.DeploymentVolume;
import com.mobaas.paas.service.KubeApiService;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.util.Yaml;

public abstract class BaseDeployer implements Deployer {

	private static final Logger log = LoggerFactory.getLogger(BaseDeployer.class);
	
	@Autowired
	protected KubeApiService apiService;
	@Autowired
	protected PaasConfig config;
	
	protected abstract void handlePaths(AppInfo appInfo, AppVersion ver, List<DeploymentVolume> volumeList);

	protected int getContainerPort() { 
		return 80; 
	}
	
	protected String getDeploymentTemplate() {
		return "deployment.yaml.ftl";
	}
	
	protected String getServiceTemplate() {
		return "service.yaml.ftl";
	}
	
	protected abstract String getContainerImage();
	
	@Override
	public void start(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) 
			throws ApiException, IOException {

		handlePaths(appInfo, ver, volumeList);
		
		V1Deployment deployment = getDeployment(appInfo, ver, envMap, volumeList);
		apiService.createDeployment(deployment);

		V1Service service = getService(appInfo);
		apiService.createService(service);
		
		applyIngress(appInfo);
	}
	
	@Override
	public void deploy(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) 
			throws ApiException, IOException {

		handlePaths(appInfo, ver, volumeList);
		
		V1Deployment deployment = getDeployment(appInfo, ver, envMap, volumeList);
		apiService.createDeployment(deployment);
		
	}
	
	@Override
	public void grayDeploy(AppInfo appInfo, AppVersion ver, AppGrayVersion gver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws ApiException, IOException {
		handlePaths(appInfo, ver, volumeList);
		
		V1Deployment deployment = getGrayDeployment(appInfo, ver, gver, envMap, volumeList);
		apiService.createDeployment(deployment);
	}
	
	@Override
	public void upgrade(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws ApiException {

		handlePaths(appInfo, ver, volumeList);
		
        // env
        List<Map<String, Object>> envlist = new ArrayList<>();
        
        if (envMap != null && envMap.size() > 0) {
        		for (String name : envMap.keySet()) {
        			Map<String, Object> env = new HashMap<>();
        			env.put("name", name);
        			env.put("value", envMap.get(name));
        			envlist.add(env);
        		}
        }
        
        Map<String, Object> customEnvMap = getCustomEnvironments(appInfo, ver);
        if (customEnvMap != null && customEnvMap.size() > 0) {
    		for (String name : customEnvMap.keySet()) {
    			Map<String, Object> env = new HashMap<>();
    			env.put("name", name);
    			env.put("value", customEnvMap.get(name));
    			envlist.add(env);
    		}
    }
        
        // volumeMounts
        List<Map<String, String>> mountlist = new ArrayList<>();
        List<Map<String, Object>> vollist = new ArrayList<>();
        for (DeploymentVolume vol : volumeList) {
	        Map<String, String> mount1 = new HashMap<>();
	        mount1.put("name", vol.getName());
	        mount1.put("mountPath", vol.getMountPath());
	        mountlist.add(mount1);
	        
	        Map<String, Object> vol1 = new HashMap<>();
	        vol1.put("name", vol.getName());
	        vol1.put("hostPath", getMap("path", vol.getHostPath()));
	        vollist.add(vol1);
	        
	        if (!StringUtils.isEmpty(vol.getEnvName())) {
		        Map<String, Object> map = new HashMap<>();
		        map.put("name", vol.getEnvName());
		        map.put("value", vol.getMountPath());
		        envlist.add(map);
	        }
        }

        List<DeploymentPatchItem> patchlist = new ArrayList<>();
        patchlist.add(new DeploymentPatchItem("replace", "/spec/template/metadata/labels/version", ver.getVersion()));
        patchlist.add(new DeploymentPatchItem("replace", "/spec/template/spec/containers/0/env", envlist));
        patchlist.add(new DeploymentPatchItem("replace", "/spec/template/spec/containers/0/volumeMounts", mountlist));
        patchlist.add(new DeploymentPatchItem("replace", "/spec/template/spec/volumes", vollist));
        
        //deploy.
        Gson gson = new Gson();
        V1Patch patch = new V1Patch(gson.toJson(patchlist));
        V1Deployment newDeploy = apiService.patchDeployment(appInfo.getAppId(), appInfo.getNamespace(), patch);
	}
	
	public void applyIngress(AppInfo appInfo) throws ApiException, IOException {

		if (!StringUtils.isEmpty(appInfo.getDomain())) {
			ExtensionsV1beta1Ingress ingress = getIngress(appInfo);
			
			ExtensionsV1beta1Ingress origin = apiService.queryIngress(appInfo.getAppId() + "-ing", appInfo.getNamespace());
			if (origin == null) {
				apiService.createIngress(ingress);
			} else {
				apiService.replaceIngress(ingress);
			}
		}
	}
	
	public void deleteIngress(AppInfo appInfo) throws ApiException, IOException {

		apiService.deleteIngress(appInfo.getAppId() + "-ing", appInfo.getNamespace(), null);
	}
	
	@Override
	public void delete(AppInfo appInfo) throws ApiException {
		apiService.deleteIngress(appInfo.getAppId() + "-ing", appInfo.getNamespace(), null);
		apiService.deleteService(appInfo.getAppId(), appInfo.getNamespace(), null);
		apiService.deleteDeployment(appInfo.getAppId(), appInfo.getNamespace(), null);
	}
	
	@Override
	public V1Deployment getDeployment(AppInfo appInfo, AppVersion ver, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws IOException {
		
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = getTemplate(getDeploymentTemplate());
       
        Map<String, Object> dataModel = getDeploymentModel(appInfo.getAppId(), appInfo.getAppId(), ver.getVersion(), appInfo.getNamespace(), appInfo.getInstanceNum(), appInfo.getReadinessPath());
        
        dataModel.put("volumelist", volumeList);
        
        Map<String, Object> envmap = new HashMap<>();
        envmap.putAll(envMap);

        Map<String, Object> customEnvMap = getCustomEnvironments(appInfo, ver);
        if (customEnvMap != null && customEnvMap.size() > 0) {
	    		envmap.putAll(customEnvMap);
	    }
        
        for (DeploymentVolume vol : volumeList) {
        		if (!StringUtils.isEmpty(vol.getEnvName())) {
        			envmap.put(vol.getEnvName(), vol.getMountPath());
        		}
        }
        dataModel.put("envmap", envmap);

        String yamlContent = mergeTemplate(template, dataModel);
        
        try {
	        File yamlFile = Paths.get( config.getAppsRoot(), "yaml", appInfo.getAppId() + "-" + ver.getVersion() + ".yaml").toFile();
        		yamlFile.deleteOnExit();
	        FileUtils.write(yamlFile, yamlContent, "utf-8");
        } catch (Exception ex) {
        		ex.printStackTrace();
        }
        
        return (V1Deployment)Yaml.load(yamlContent);
	}
	
	@Override
	public V1Deployment getGrayDeployment(AppInfo appInfo, AppVersion ver, AppGrayVersion grayVer, Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws IOException {
		
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = getTemplate(getDeploymentTemplate());
       
        String grayDeployName = appInfo.getAppId() + "-" + grayVer.getVersion();
        String appVersion = grayVer.getVersion() + "-gray";
        Map<String, Object> dataModel = getDeploymentModel(grayDeployName, appInfo.getAppId(), appVersion, appInfo.getNamespace(), grayVer.getInstanceNum(), appInfo.getReadinessPath());
        
        dataModel.put("volumelist", volumeList);
        
        Map<String, Object> envmap = new HashMap<>();
        envmap.putAll(envMap);

        Map<String, Object> customEnvMap = getCustomEnvironments(appInfo, ver);
        if (customEnvMap != null && customEnvMap.size() > 0) {
	    		envmap.putAll(customEnvMap);
	    }
        
        for (DeploymentVolume vol : volumeList) {
        		if (!StringUtils.isEmpty(vol.getEnvName())) {
        			envmap.put(vol.getEnvName(), vol.getMountPath());
        		}
        }
        dataModel.put("envmap", envmap);

        String yamlContent = mergeTemplate(template, dataModel);
        
        try {
	        File yamlFile = Paths.get( config.getAppsRoot(), "yaml", appInfo.getAppId() + "-" + ver.getVersion() + ".yaml").toFile();
        		yamlFile.deleteOnExit();
	        FileUtils.write(yamlFile, yamlContent, "utf-8");
        } catch (Exception ex) {
        		ex.printStackTrace();
        }
        
        return (V1Deployment)Yaml.load(yamlContent);
	}
	
	@Override
	public V1Service getService(AppInfo appInfo) throws IOException {
		
        Template template = getTemplate(getServiceTemplate());

        Map<String, Object> dataModel = getServiceModel(appInfo);
        
        String yamlContent = mergeTemplate(template, dataModel);
        return (V1Service)Yaml.load(yamlContent);
	}

	private ExtensionsV1beta1Ingress getIngress(AppInfo appInfo) throws IOException {

        // 第四步：加载一个模板，创建一个模板对象。
        Template template = getTemplate("ingress.yaml.ftl");
        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        Map<String, Object> dataModel = new HashMap<>();
        //向数据集中添加数据
        dataModel.put("ingName", appInfo.getAppId()+"-ing");
        dataModel.put("namespace", appInfo.getNamespace());
        dataModel.put("domains", appInfo.getDomain().split(","));
        dataModel.put("svcName", StringUtils.isEmpty(appInfo.getServiceId()) ? appInfo.getAppId() : appInfo.getServiceId());
        
        if (appInfo.getEnableTls() == 1) {
        		dataModel.put("enableTls", true);
        		dataModel.put("tlsSecretName", appInfo.getAppId()+"-tls");
        } else {
        		dataModel.put("enableTls", false);
        }
        
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(outStream);
        try {
	        // 第七步：调用模板对象的process方法输出文件。
	        template.process(dataModel, out);
	        // 第八步：关闭流。
	        out.close();
        } catch (TemplateException ex) {
        		throw new IOException(ex);
        }
        
        return (ExtensionsV1beta1Ingress)Yaml.load(new String(outStream.toByteArray(), "utf-8"));
       
	}
	
	protected Map<String, Object> getCustomEnvironments(AppInfo appInfo, AppVersion version) {
		return null;
	}
	
	protected Map<String, Object> getDeploymentModel(String deployName, String appName, String appVersion, String namespace, int instanceNum, String readinessPath) {
		 // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        Map<String, Object> dataModel = new HashMap<>();
        //向数据集中添加数据
        dataModel.put("deployeeName", deployName);
        dataModel.put("appName", appName);
        dataModel.put("namespace", namespace);
        dataModel.put("appVersion", appVersion);
        dataModel.put("instanceNum", instanceNum);
        dataModel.put("containerImage", getContainerImage());
        dataModel.put("containerPort", getContainerPort());
        dataModel.put("readinessPath", readinessPath);
        return dataModel;
	}
	
	protected Map<String, Object> getServiceModel(AppInfo appInfo) {
		Map<String, Object> dataModel = new HashMap<>();
        //向数据集中添加数据
        dataModel.put("appName", appInfo.getAppId());
        dataModel.put("namespace", appInfo.getNamespace());
        dataModel.put("containerPort", getContainerPort());
        	dataModel.put("svcName", StringUtils.isEmpty(appInfo.getServiceId()) ? appInfo.getAppId() : appInfo.getServiceId());
        
        return dataModel;
	}
	
	private static Map<String, Object> getMap(String key, String value) {
		Map<String, Object> map = new HashMap<>();
		map.put(key, value);
		return map;
	}
	
	protected String mergeTemplate(Template template, Map<String, Object> dataModel) throws IOException {
		 // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(outStream);
        try {
	        // 第七步：调用模板对象的process方法输出文件。
	        template.process(dataModel, out);
	        // 第八步：关闭流。
	        out.close();
	        
	        return new String(outStream.toByteArray(), "utf-8");
        } catch (TemplateException ex) {
        		throw new IOException(ex);
        }
	}
	
	protected Template getTemplate(String yaml) throws IOException {
		// 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 第二步：设置模板文件所在的路径。
        configuration.setClassForTemplateLoading(WebApplication.class, "/kubernetes");
        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        // 第四步：加载一个模板，创建一个模板对象。
        return configuration.getTemplate(yaml);	
	}

	protected void unzip(InputStream inStream, Path target) throws IOException, ArchiveException {
		
        BufferedInputStream bis = new BufferedInputStream(inStream);
        
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, bis, "utf-8");
	    	 
         ZipArchiveEntry zipEntry = null;
         while ((zipEntry = (ZipArchiveEntry) in.getNextEntry()) != null) {
        	 	try {
	             if (zipEntry.isDirectory()) {
	         	 	Path path = target.resolve(zipEntry.getName());
	         	 	if (!Files.exists(path)) {
	         	 		Files.createDirectories(path);
	         	 	}
	             } else {
	            	 	Path path = target.resolve(zipEntry.getName());
	                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING); 
	             }
        	 	} catch (Exception ex) {
        	 		log.error(ex.getLocalizedMessage());
        	 	}
         }
	        
         bis.close();
	}
}
