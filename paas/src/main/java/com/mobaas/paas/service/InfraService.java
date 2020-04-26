/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mobaas.paas.PageList;
import com.mobaas.paas.model.DockerInfo;
import com.mobaas.paas.model.Host;

/**
 * 
 * @author billy zhang
 * 
 */
public interface InfraService {

	// docker
	DockerInfo selectDockerInfoByNo(String dockerNo);

	PageList<DockerInfo> selectDockerInfoList(int pageNo, int pageSize);

	PageList<Host> selectHostList(String ip, int pageNo, int pageSize);

	Host selectHostById(int id);

	Host selectHostByName(String name);
	
	Host selectHostByIp(String hostIp);

	Integer selectHostTotal();

}
