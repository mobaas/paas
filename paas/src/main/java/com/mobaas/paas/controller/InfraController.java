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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mobaas.paas.PageList;
import com.mobaas.paas.PagerInfo;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.service.InfraService;
import com.mobaas.paas.service.KubeApiService;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;

import com.mobaas.paas.model.Host;

@Controller
@RequestMapping(value = "/infr")
public class InfraController  extends BaseController {
	
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private InfraService infrService;
	@Resource
	private KubeApiService kubeService;
	@Resource
	private PaasConfig config;
    
    @GetMapping(value = "hostlist")
    public ModelAndView hostList(
    			@RequestParam(value = "group", defaultValue = "0", required = false) int group,
    			@RequestParam(value = "ip", defaultValue = "", required = false) String ip,
    			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView model = new ModelAndView();

        PagerInfo pager = new PagerInfo(pageNo, pageSize);
        PageList<Host> pglist = infrService.selectHostList(group, ip, pageNo, pageSize);
        
        Map<String, Integer> instMap = queryInstanceNum();
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
        model.addObject("group", group);
        model.addObject("ip", ip);

        model.setViewName("infr/hostlist");

        return model;
    }
    
	private Map<String, Integer> queryInstanceNum() {

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
}
