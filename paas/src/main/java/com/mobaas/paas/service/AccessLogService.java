package com.mobaas.paas.service;

import java.util.Date;
import java.util.List;

import com.mobaas.paas.model.AccessLog;
import com.mobaas.paas.model.AccessTotal;

public interface AccessLogService {

	void insertAccessLog(AccessLog log);

	List<AccessTotal> selectAccessTotalListByDate(String date, int offset, int limit);

	List<AccessTotal> selectAccessTotalListByService(String service, String start, int interval);
	
	AccessTotal selectAccessTotalLast(Date start, int interval);
}
