package com.mobaas.paas.kubernetes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.WebApplication;
import com.mobaas.paas.deploy.Deployer;
import com.mobaas.paas.deploy.DeployerFactory;
import com.mobaas.paas.kubernetes.DeploymentVolume;
import com.mobaas.paas.service.AppService;
import com.mobaas.paas.service.KubeApiService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.models.ExtensionsV1beta1HTTPIngressPath;
import io.kubernetes.client.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.models.ExtensionsV1beta1IngressRule;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServicePort;
import io.kubernetes.client.util.Yaml;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KubeTest {

	@Autowired
	private KubeApiService kubeService;
	@Autowired
	private AppService appService;
	@Autowired
	private ObjectMapper jsonMapper;
	@Autowired
	private DeployerFactory deployerFactory;
	@Autowired
	private ApplicationContext context;
	
	@Test
	public void testConnect() throws IOException, ApiException {
		
		String namespace = "default";
		V1Service srv = kubeService.queryService("kfayun-portal", namespace);
	    System.out.println(srv.getMetadata().getName());
    
	    V1PodList podlist = kubeService.queryPodList("kfayun-portal", namespace);
	    for (V1Pod item : podlist.getItems()) {
	      System.out.println(item.getMetadata().getName());
	    }
	    
	    V1Deployment deploy = kubeService.queryDeployment("kfayun-portal", namespace);
	    System.out.println(deploy.getMetadata().getName());
	    
	    ExtensionsV1beta1Ingress ing = kubeService.queryIngress("kfayun-portal-ing", namespace);
	    System.out.println(ing.getMetadata().getName());
	}
	
	@Test
	public void testServiceYaml() throws IOException, TemplateException {
		
		AppInfo appInfo = appService.selectAppInfoById("kfayun-portal");
		Deployer deployer = deployerFactory.createDeployer(appInfo.getPlatform());
		
        V1Service srv = deployer.getService(appInfo);
        assertEquals("kfayun-portal", srv.getMetadata().getName());
        assertTrue(srv.getSpec().getSelector().containsKey("k8s-app"));
        assertEquals("kfayun-portal", srv.getSpec().getSelector().get("k8s-app"));
        
        assertEquals(1, srv.getSpec().getPorts().size());
        V1ServicePort port = srv.getSpec().getPorts().get(0);
        assertEquals(new Integer(8080), port.getTargetPort().getIntValue());
	}
	
	@Test
	public void testDeploymentYaml() throws IOException, TemplateException, ApiException {
		
		Deployer deployer = deployerFactory.createDeployer("java");
		context.getAutowireCapableBeanFactory().autowireBean(deployer);
		
		AppInfo appInfo = appService.selectAppInfoById("makingrobot-web");
		AppVersion ver = appService.selectAppVersionByVersion("cu-makingrobot", "0.8.10");
		
		Map<String, Object> envMap = new HashMap<>();
		List<DeploymentVolume> volumeList = new ArrayList<>();
		getDeploymentData(appInfo, ver, envMap, volumeList);
		
		V1Deployment deploy = deployer.getDeployment(appInfo, ver, envMap, volumeList);
		
        assertEquals("makingrobot-web", deploy.getMetadata().getName());
        
	}
	
	private void getDeploymentData(AppInfo appInfo, AppVersion ver, 
			Map<String, Object> envMap, List<DeploymentVolume> volumeList) throws IOException {
		envMap.put("TZ", "Asia/Shanghai");
	
		if (!StringUtils.isEmpty(appInfo.getEnvironments())) {
	    		Map<String, Object> map = (Map<String, Object>)jsonMapper.readValue(appInfo.getEnvironments(), Map.class);
	    		envMap.putAll(map);
		}
	
		boolean customUpload = false;
		if (!StringUtils.isEmpty(appInfo.getVolumes())) {
			DeploymentVolume[] volumes = jsonMapper.readValue(appInfo.getVolumes(), DeploymentVolume[].class);
			for (int i=0; i<volumes.length; i++) {
				volumeList.add(volumes[i]);
				if ("upload".equals(volumes[i].getName())) {
					customUpload = true;
				}
			}
		}
		
		volumeList.add(new DeploymentVolume("home", String.format("/mnt/nas/apps/%s/%s", appInfo.getAppId(), ver.getVersion()), "/home", null));
		if (!customUpload) {
			volumeList.add(new DeploymentVolume("upload", String.format("/mnt/nas/apps/%s/upload", appInfo.getAppId()), "/home/upload", "kfayun.upload-path"));
		}
	}
}
