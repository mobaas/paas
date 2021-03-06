/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mobaas.paas.PageList;
import com.mobaas.paas.PagerInfo;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.service.InfraService;
import com.mobaas.paas.service.InstanceService;
import com.mobaas.paas.service.KubeApiService;
import com.mobaas.paas.service.MetricsService;
import com.mobaas.paas.util.DateUtil;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;

import com.mobaas.paas.model.DockerInfo;
import com.mobaas.paas.model.Host;
import com.mobaas.paas.model.NodeMetrics;

@Controller
@RequestMapping(value = "/infr")
public class InfraController  extends BaseController {
	
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private InfraService infrService;
    @Autowired
    private MetricsService metricsService;
	@Resource
	private KubeApiService kubeService;
	@Resource
	private InstanceService instService;
	@Resource
	private PaasConfig config;
    
	@GetMapping(value = "dockerlist")
    public ModelAndView dockerList(
    			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView model = new ModelAndView();

        PagerInfo pager = new PagerInfo(pageNo, pageSize);
        PageList<DockerInfo> pglist = infrService.selectDockerInfoList(pageNo, pageSize);
        pager.setTotalCount(pglist.getTotal());
        
        model.addObject("list", pglist.getList());
        model.addObject("pager", pager);

        model.setViewName("infr/dockerlist");

        return model;
    }

    @GetMapping(value = "hostlist")
    public ModelAndView hostList(
    			@RequestParam(value = "ip", defaultValue = "", required = false) String ip,
    			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView model = new ModelAndView();

        PagerInfo pager = new PagerInfo(pageNo, pageSize);
        PageList<Host> pglist = infrService.selectHostList(ip, pageNo, pageSize);
        
        Map<String, Integer> instMap = instService.queryInstanceNumForNode();
	    if (instMap != null) {
	        for (Host host : pglist.getList()) {
	    		if (instMap.containsKey(host.getHostIp())) {
	    			host.setInstanceNum(instMap.get(host.getHostIp()));
	    		} else if (instMap.containsKey(host.getOuterIp())) {
	    			host.setInstanceNum(instMap.get(host.getOuterIp()));
	    		}
	        }
        }
        
        pager.setTotalCount(pglist.getTotal());
        
        model.addObject("list", pglist.getList());
       
        model.addObject("pager", pager);
        model.addObject("ip", ip);

        model.setViewName("infr/hostlist");

        return model;
    }
    
    @GetMapping(value = "hostmetrics")
    public ModelAndView hostMetrics(
    		@RequestParam(value = "id") int id,
    		@RequestParam(value = "kind", required=false, defaultValue="1m") String kind) {

    		Host host  = infrService.selectHostById(id);
    		
    		List<NodeMetrics> list = metricsService.selectNodeMetricsList(host.getName(), kind, 60);
    		List<NodeMetrics> list2 = new ArrayList<>(list);
    		Collections.reverse(list2);
    		
    		List<String> dateList = new ArrayList<>();
    		List<Integer> cpuList = new ArrayList<>();
    		List<Integer> memoryList = new ArrayList<>();
    		
    		list2.forEach( (usage)-> {
    			dateList.add("\"" + DateUtil.toHhMmString(usage.getTimestamp()) + "\"");
    			cpuList.add( usage.getCpuUsage() / 1000000);  //ms
    			memoryList.add( usage.getMemoryUsage() / 1024);  // mb
    		});
    		
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("dateList", StringUtils.collectionToCommaDelimitedString(dateList) );
    		mv.addObject("cpuList", StringUtils.collectionToCommaDelimitedString(cpuList) );
    		mv.addObject("memoryList", StringUtils.collectionToCommaDelimitedString(memoryList) );
    		mv.addObject("memoryMax", host.getMemory());
    		mv.addObject("cpuMax", host.getCpuNum() * 1000); // ms
    		
    		mv.addObject("id", id);
    		mv.addObject("kind", kind);
    		
    		mv.setViewName("/infr/hostmetrics");
    		
    		return mv;
    }

	
}
