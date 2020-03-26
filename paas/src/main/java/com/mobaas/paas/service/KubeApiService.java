/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import com.mobaas.kubernetes.metrics.models.MetricsV1beta1NodeMetrics;
import com.mobaas.kubernetes.metrics.models.MetricsV1beta1NodeMetricsList;
import com.mobaas.kubernetes.metrics.models.MetricsV1beta1PodMetrics;
import com.mobaas.kubernetes.metrics.models.MetricsV1beta1PodMetricsList;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.models.V1beta1EventList;

public interface KubeApiService {

	/*
	 * kubectl get pod -n namespace
	 */
	V1PodList queryPodList(String deployName, String namespace) throws ApiException;
	
	/*
	 * kubectl get pod podName -n namespace
	 */
	V1Pod queryPod(String podName, String namespace) throws ApiException;
	
	String getPodLog(String podName, String namespace, Integer tailLines) throws ApiException;
	
	V1Status deletePod(String podName, String namespace, V1DeleteOptions body) throws ApiException;
	
	/// Service ///
	
	/*
	 * kubectl get svc serviceName -n namespace
	 */
	V1Service queryService(String serviceName, String namespace) throws ApiException;

	V1Service createService(V1Service service) throws ApiException;
	
	V1Service replaceService(V1Service service) throws ApiException;
	
	V1Service patchService(String serviceName, String namespace, V1Patch body) throws ApiException;
	
	V1Status deleteService(String serviceName, String namespace, V1DeleteOptions body) throws ApiException;
	
	/// Deployment ///
	
	/*
	 * kubectl get deploy deployName -n namespace
	 */
	V1Deployment queryDeployment(String deployName, String namespace) throws ApiException;

	V1Deployment createDeployment(V1Deployment body) throws ApiException;
	
	V1Deployment replaceDeployment(V1Deployment deployment) throws ApiException;
	
	/*
	 * kubectl patch deploy deployName -n namespace --patch ...
	 */
	V1Deployment patchDeployment(String deployName, String namespace, V1Patch body) throws ApiException;

	V1Status deleteDeployment(String deployName, String namespace, V1DeleteOptions body) throws ApiException;
	
	/*
	 * kubectl get ing ingName -n namespace
	 */
	ExtensionsV1beta1Ingress queryIngress(String ingressName, String namespace) throws ApiException;
	
	ExtensionsV1beta1Ingress createIngress(ExtensionsV1beta1Ingress ingress) throws ApiException;
	
	ExtensionsV1beta1Ingress replaceIngress(ExtensionsV1beta1Ingress ingress) throws ApiException;
	
	ExtensionsV1beta1Ingress patchIngress(String ingressName, String namespace, V1Patch body) throws ApiException;
	
	V1Status deleteIngress(String deploymentName, String namespace, V1DeleteOptions body) throws ApiException;

	/*
	 * kubectl top nodes
	 */
	MetricsV1beta1NodeMetricsList queryNodeMetricsList() throws ApiException;
	
	/*
	 * kubectl top node xxx
	 */
	MetricsV1beta1NodeMetrics queryNodeMetrics(String nodName) throws ApiException;
	
	/*
	 * kubectl top pods -n namespace
	 */
	MetricsV1beta1PodMetricsList queryPodMetricsList(String namespace) throws ApiException;
	
	/*
	 * kubectl top pod podName -n namespace
	 */
	MetricsV1beta1PodMetrics queryPodMetrics(String podName, String namespace) throws ApiException;
	
	/*
	 * event
	 */
	V1beta1EventList queryEventList(String deploymentName, String namespace) throws ApiException;
	
	/*
	 * namespace
	 */
	V1NamespaceList queryNamespaceList() throws ApiException;
}
