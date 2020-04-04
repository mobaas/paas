package com.mobaas.paas.service;

import com.mobaas.paas.model.NodeMetrics;
import com.mobaas.paas.model.PodMetrics;

public interface MonitoringService {

	void handlePodMetrics(PodMetrics metrics);

	void handleNodeMetrics(NodeMetrics metrics);

}
