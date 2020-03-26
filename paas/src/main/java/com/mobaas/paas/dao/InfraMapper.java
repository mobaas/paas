/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mobaas.paas.model.Host;

public interface InfraMapper {

	Host selectHostByIp(
			@Param("hostIp")String hostIp);

	List<Host> selectHostListByGroup(
			@Param("groupId")int groupId,
			@Param("state")int state);

	int selectHostCount(
			@Param("groupId")int groupId,
			@Param("ip")String ip);
	
	List<Host> selectHostList(
			@Param("groupId")int groupId,
			@Param("ip")String ip,
			@Param("offset")int offset,
			@Param("limit")int limit);

	List<Map> selectHostTotalForGroup();
	
	int selectHostTotal();
	
}
