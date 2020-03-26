/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.gateway.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class DiscoveryController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ObjectMapper objectMapper;
    
    @GetMapping(value = {"/", "index"})
    public String index(){

        return "It works.";
	        
    }
    
    /**
     * 返回远程调用的结果
     * @return
     */
    @RequestMapping("/servicedetail")
    public String serviceDetail (
            @RequestParam("name") String servicename) throws IOException {
        return "Service [" + servicename + "]'s instance list : " + objectMapper.writeValueAsString(discoveryClient.getInstances(servicename));
    }

    /**
     * 返回发现的所有服务
     * @return
     */
    @RequestMapping("/services")
    public String services() {
        return this.discoveryClient.getServices().toString()
                + ", "
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
