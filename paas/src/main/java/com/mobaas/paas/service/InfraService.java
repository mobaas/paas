/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mobaas.paas.PageList;
import com.mobaas.paas.model.Host;

/**
 * 
 * @author billy zhang
 * 
 */
public interface InfraService {

	PageList<Host> selectHostList(int groupId, String ip, int pageNo, int pageSize);

	List<Host> selectHostListByGroup(int groupId, int state);

	Host selectHostById(int id);
	
	Host selectHostByIp(String hostIp);

	Map<Integer, Integer> selectHostTotalForGroup();

	Integer selectHostTotal();

}
