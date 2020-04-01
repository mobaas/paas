package com.mobaas.paas.kubernetes;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mobaas.kubernetes.models.MetricsV1beta1NodeMetrics;
import com.mobaas.kubernetes.models.MetricsV1beta1NodeMetricsList;
import com.mobaas.kubernetes.models.MetricsV1beta1PodContainer;
import com.mobaas.kubernetes.models.MetricsV1beta1PodMetrics;
import com.mobaas.kubernetes.models.MetricsV1beta1PodMetricsList;
import com.mobaas.paas.service.KubeApiService;

import io.kubernetes.client.ApiException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KubeApiTest {

	@Autowired
	private KubeApiService kubeService;
	
	@Test
	public void testConnect() throws IOException, ApiException {
		
		MetricsV1beta1NodeMetricsList metricsList = kubeService.queryNodeMetricsList();
	    for (MetricsV1beta1NodeMetrics item : metricsList.getItems()) {
	    		System.out.println(item);
	    }
	    
	    MetricsV1beta1PodMetricsList metricsList2 = kubeService.queryPodMetricsList("kfayun");
	    for (MetricsV1beta1PodMetrics item : metricsList2.getItems()) {
	    		System.out.println(item);
	    		for (MetricsV1beta1PodContainer container : item.getContainers()) {
	    			System.out.println(container);
	    		}
	    }
	}
	
	
}
