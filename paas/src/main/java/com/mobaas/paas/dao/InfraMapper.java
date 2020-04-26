/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mobaas.paas.model.DockerInfo;
import com.mobaas.paas.model.Host;

public interface InfraMapper {

	DockerInfo selectDockerInfoByNo(
			@Param("dockerNo")String dockerNo);
	
	DockerInfo selectDockerInfoByAppId(
			@Param("appId")String appId);
	
	Host selectHostById(
			@Param("id")int id);
	
	Host selectHostByIp(
			@Param("hostIp")String hostIp);

	Host selectHostByName(
			@Param("name")String name);
	
	int selectHostCount(
			@Param("ip")String ip);
	
	List<Host> selectHostList(
			@Param("ip")String ip,
			@Param("offset")int offset,
			@Param("limit")int limit);

	int selectHostTotal();

	int selectDockerInfoCount();

	List<DockerInfo> selectDockerInfoList(
			@Param("offset")int offset,
			@Param("limit")int limit);

}
