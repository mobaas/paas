package com.mobaas.paas.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobaas.paas.dao.AccessLogMapper;
import com.mobaas.paas.model.AccessLog;
import com.mobaas.paas.model.AccessTotal;
import com.mobaas.paas.service.AccessLogService;


@Service
public class AccessLogServiceImpl implements AccessLogService {

	@Autowired
	private AccessLogMapper mapper;
	
	@Override
	public void insertAccessLog(AccessLog log) {
		mapper.insertAccessLog(log);
	}

	@Override
	public List<AccessTotal> selectAccessTotalListByDate(String date, int offset, int limit) {
		return mapper.selectAccessTotalListByDate(date, offset, limit);
	}

	@Override
	public List<AccessTotal> selectAccessTotalListByService(String service, String start, int interval) {
		return mapper.selectAccessTotalListByService(service, start, interval);
	}

	@Override
	public AccessTotal selectAccessTotalLast(Date start, int interval) {
		return mapper.selectAccessTotalLast(start, interval);
	}
	
}
