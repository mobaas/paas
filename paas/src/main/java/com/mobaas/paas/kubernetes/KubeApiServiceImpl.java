/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.kubernetes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.mobaas.kubernetes.apis.MetricsV1beta1Api;
import com.mobaas.kubernetes.models.MetricsV1beta1NodeMetrics;
import com.mobaas.kubernetes.models.MetricsV1beta1NodeMetricsList;
import com.mobaas.kubernetes.models.MetricsV1beta1PodMetrics;
import com.mobaas.kubernetes.models.MetricsV1beta1PodMetricsList;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.service.KubeApiService;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.AppsV1Api;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.apis.EventsV1beta1Api;
import io.kubernetes.client.apis.ExtensionsV1beta1Api;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.models.V1beta1EventList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;

public class KubeApiServiceImpl implements KubeApiService {

	private static final Logger log = LoggerFactory.getLogger(KubeApiServiceImpl.class);
	
	private CoreV1Api coreApi;
	private AppsV1Api appsApi;
	private ExtensionsV1beta1Api extApi;
	private EventsV1beta1Api eventApi;
	private MetricsV1beta1Api metricsApi;
	
	static {
		Yaml.addModelMap("apps/v1", "Deployment", V1Deployment.class);
		Yaml.addModelMap("v1", "Service", V1Service.class);
		Yaml.addModelMap("extensions/v1beta1", "Ingress", ExtensionsV1beta1Ingress.class);
	}
	
	public KubeApiServiceImpl(PaasConfig config) throws IOException {
		// file path to your KubeConfig
		InputStream inStream = new FileInputStream(config.getKubeConfig());
		
	    // loading the out-of-cluster config, a kubeconfig from file-system
	    ApiClient client =
	        ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new InputStreamReader(inStream))).build();

	    // set the global default api-client to the in-cluster one from above
	    Configuration.setDefaultApiClient(client);

	    // the CoreV1Api loads default api-client from global configuration.
	    coreApi = new CoreV1Api();

	    appsApi = new AppsV1Api();
	    
	    extApi = new ExtensionsV1beta1Api();
	    
	    eventApi = new EventsV1beta1Api();
	    
	    metricsApi = new MetricsV1beta1Api();
	}
	
	@Override
	public V1Service queryService(String serviceName, String namespace) throws ApiException {
		try {
			return coreApi.readNamespacedService(serviceName, namespace, null, null, null);
		} catch (ApiException ex) {
			log.warn("Query Service: " + ex.getLocalizedMessage());
			return null;
		}
	}

	@Override
	public V1Service createService(V1Service body) throws ApiException {
		String namespace = StringUtils.isEmpty(body.getMetadata().getNamespace()) 
				? "default" : body.getMetadata().getNamespace();
		return coreApi.createNamespacedService(namespace, body, null, null, null);
	}
	
	@Override
	public V1Service replaceService(V1Service body) throws ApiException {
		String namespace = StringUtils.isEmpty(body.getMetadata().getNamespace()) 
				? "default" : body.getMetadata().getNamespace();
		return coreApi.replaceNamespacedService(body.getMetadata().getName(), 
				namespace, body, null, null, null);
	}

	@Override
	public V1Service patchService(String serviceName, String namespace, V1Patch body) throws ApiException {

		return coreApi.patchNamespacedService(serviceName, namespace, body, null, null, null, null);
	}

	@Override
	public V1Status deleteService(String serviceName, String namespace, V1DeleteOptions body) throws ApiException {
		return coreApi.deleteNamespacedService(serviceName, namespace, null, body, null, null, null, null);
	}
	
	@Override
	public V1Deployment queryDeployment(String deployName, String namespace) throws ApiException {
		try {
			return appsApi.readNamespacedDeployment(deployName, namespace, null, null, null);
		} catch (ApiException ex) {
			log.warn("Query Deployment: " + ex.getLocalizedMessage());
			return null;
		}
	}

	@Override
	public V1Deployment createDeployment(V1Deployment body) throws ApiException {
		String namespace = StringUtils.isEmpty(body.getMetadata().getNamespace()) 
				? "default" : body.getMetadata().getNamespace();
		return appsApi.createNamespacedDeployment(namespace, body, null, null, null);
	}
	
	@Override
	public V1Deployment replaceDeployment(V1Deployment body) throws ApiException {
		String namespace = StringUtils.isEmpty(body.getMetadata().getNamespace()) 
				? "default" : body.getMetadata().getNamespace();
		return appsApi.replaceNamespacedDeployment(body.getMetadata().getName(), 
				namespace, body, null, null, null);
		
	}

	@Override
	public V1Deployment patchDeployment(String deployName, String namespace, V1Patch body) throws ApiException {

		return appsApi.patchNamespacedDeployment(deployName, namespace, body, null, null, null, null);
	}
	
	@Override
	public V1Status deleteDeployment(String deploymentName, String namespace, V1DeleteOptions body) throws ApiException {
		return appsApi.deleteNamespacedDeployment(deploymentName, namespace, null, body, null, null, null, null);
	}
	
	@Override
	public ExtensionsV1beta1Ingress queryIngress(String ingressName, String namespace) throws ApiException {
		try {
			return extApi.readNamespacedIngress(ingressName, namespace, null, null, null);
		} catch (Exception ex) {
			log.warn("Query Ingress: " + ex.getLocalizedMessage());
			return null;
		}
	}
	
	@Override
	public ExtensionsV1beta1Ingress createIngress(ExtensionsV1beta1Ingress body) throws ApiException {
		String namespace = StringUtils.isEmpty(body.getMetadata().getNamespace()) 
				? "default" : body.getMetadata().getNamespace();
		return extApi.createNamespacedIngress(namespace, body, null, null, null);
		
	}
	
	@Override
	public ExtensionsV1beta1Ingress replaceIngress(ExtensionsV1beta1Ingress body) throws ApiException {
		String namespace = StringUtils.isEmpty(body.getMetadata().getNamespace()) 
				? "default" : body.getMetadata().getNamespace();
		return extApi.replaceNamespacedIngress(body.getMetadata().getName(), 
				namespace, body, null, null, null);
		
	}

	@Override
	public ExtensionsV1beta1Ingress patchIngress(String ingressName, String namespace, V1Patch body) throws ApiException {

		return extApi.patchNamespacedIngress(ingressName, namespace, body, null, null, null, null);
	}
	
	@Override
	public V1Status deleteIngress(String ingressName, String namespace, V1DeleteOptions body) throws ApiException {
		return extApi.deleteNamespacedIngress(ingressName, namespace, null, body, null, null, null, null);
	}
	
	@Override
	public V1PodList queryPodList(String deployName, String namespace) throws ApiException {
		String labelSelector = null;
		if (deployName != null)
			labelSelector = "k8s-app=" + deployName;
		return coreApi.listNamespacedPod(namespace, null, null, null, labelSelector, null, null, null, null);
	}
	
	@Override
	public V1Pod queryPod(String podName, String namespace) throws ApiException {
		try {
			return coreApi.readNamespacedPod(podName, namespace, null, null, null);
		} catch (ApiException ex) {
			log.warn("Query Pod: " + ex.getLocalizedMessage());
			return null;
		}
	}

	@Override
	public String getPodLog(String podName, String namespace, Integer tailLines) throws ApiException {
		return coreApi.readNamespacedPodLog( podName, namespace, null, null, null, null, null, null, tailLines, null);
	}
	
	@Override
	public V1Status deletePod(String podName, String namespace, V1DeleteOptions body) throws ApiException {
		return coreApi.deleteNamespacedPod(podName, namespace, null, body, null, null, null, null);
	}

	@Override
	public V1NamespaceList queryNamespaceList() throws ApiException {
		return coreApi.listNamespace(null, null, null, null, null, null, null, null);
	}
	
	@Override
	public V1beta1EventList queryEventList(String deployName, String namespace) throws ApiException {
		String labelSelector = null;
		if (deployName != null)
			labelSelector = "k8s-app=" + deployName;
		return eventApi.listNamespacedEvent(namespace, null, null, null, labelSelector, null, null, null, null);
	}
	
	@Override
	public MetricsV1beta1NodeMetricsList queryNodeMetricsList() throws ApiException {

		return metricsApi.listNodeMetrics(null, null, null, null, null, null, null, null);
	}
	
	@Override
	public MetricsV1beta1NodeMetrics queryNodeMetrics(String nodeName) throws ApiException {
		try {
			return metricsApi.readNodeMetrics(nodeName, null, null, null);
		} catch (ApiException ex) {
			log.warn("Query NodeMetrics: " + ex.getLocalizedMessage());
			return null;
		}
	}
	
	@Override
	public MetricsV1beta1PodMetricsList queryPodMetricsList(String namespace) throws ApiException {

		return metricsApi.listNamespacedPodMetrics(namespace, null, null, null, null, null, null, null, null);
	}
	
	@Override
	public MetricsV1beta1PodMetrics queryPodMetrics(String podName, String namespace) throws ApiException {
		try {
			return metricsApi.readNamespacedPodMetrics(podName, namespace, null, null, null);
		} catch (ApiException ex) {
			log.warn("Query PodMetrics: " + ex.getLocalizedMessage());
			return null;
		}
	}
	
}
