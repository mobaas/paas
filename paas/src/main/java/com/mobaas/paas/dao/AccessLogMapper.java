package com.mobaas.paas.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mobaas.paas.model.AccessLog;
import com.mobaas.paas.model.AccessTotal;

public interface AccessLogMapper {

	List<AccessTotal> selectAccessTotalListByDate(
			@Param("date")String date, 
			@Param("offset")int offset, 
			@Param("limit")int limit);
	
	List<AccessTotal> selectAccessTotalListByService(
			@Param("service")String service,
			@Param("start")String start,
			@Param("interval")int interval);

	void insertAccessLog(AccessLog log);
	
	AccessTotal selectAccessTotalLast(
			@Param("start")Date start,
			@Param("interval")int interval);
}
