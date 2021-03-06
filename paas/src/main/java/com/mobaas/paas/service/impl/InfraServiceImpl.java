/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobaas.paas.PageList;
import com.mobaas.paas.model.DockerInfo;
import com.mobaas.paas.model.Host;
import com.mobaas.paas.dao.InfraMapper;
import com.mobaas.paas.service.InfraService;

@Service
public class InfraServiceImpl implements InfraService {

	@Autowired
	private InfraMapper infraMapper;

	@Override
	public DockerInfo selectDockerInfoByNo(String dockerNo) {
		return infraMapper.selectDockerInfoByNo(dockerNo);
	}

	@Override
	public Host selectHostById(int id) {
		return infraMapper.selectHostById(id);
	}

	@Override
	public Host selectHostByIp(String hostIp) {
		return infraMapper.selectHostByIp(hostIp);
	}
	
	public Host selectHostByName(String name) {
		return infraMapper.selectHostByName(name);
	}
	
	@Override
	public PageList<Host> selectHostList(String ip, int pageNo, int pageSize) {

		PageList<Host> pglist = new PageList<>();
		pglist.setTotal( infraMapper.selectHostCount(ip));
		if (pglist.getTotal() > 0) {
			int offset = (pageNo-1) * pageSize;
			pglist.setList( infraMapper.selectHostList(ip, offset, pageSize) );
		}
		
		return pglist;
	}

	@Override
	public Integer selectHostTotal() {
		return infraMapper.selectHostTotal();

	}

	@Override
	public PageList<DockerInfo> selectDockerInfoList(int pageNo, int pageSize) {
		PageList<DockerInfo> pglist = new PageList<>(pageNo, pageSize);
		pglist.setTotal(infraMapper.selectDockerInfoCount());
		if (pglist.getTotal() > 0) {
			pglist.setList(infraMapper.selectDockerInfoList(pglist.getOffset(), pageSize));
		}
		return null;
	}
	
}
