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
import com.mobaas.paas.model.Host;
import com.mobaas.paas.dao.InfraMapper;
import com.mobaas.paas.service.InfraService;

@Service
public class InfraServiceImpl implements InfraService {

	@Autowired
	private InfraMapper infraMapper;

	@Override
	public Host selectHostByIp(String hostIp) {
		return infraMapper.selectHostByIp(hostIp);
	}

	@Override
	public List<Host> selectHostListByGroup(int groupId, int state) {
		return infraMapper.selectHostListByGroup(groupId, state);
	}

	@Override
	public PageList<Host> selectHostList(int groupId, String ip, int pageNo, int pageSize) {
		PageList<Host> pglist = new PageList<>();
		pglist.setTotal( infraMapper.selectHostCount(groupId, ip));
		if (pglist.getTotal() > 0) {
			int offset = (pageNo-1) * pageSize;
			pglist.setList( infraMapper.selectHostList(groupId, ip, offset, pageSize) );
		}
		
		return pglist;
	}

	@Override
	public Map<Integer, Integer> selectHostTotalForGroup() {
		List<Map> list = infraMapper.selectHostTotalForGroup();
		return list.stream().collect(Collectors.toMap(map->(Integer)map.get("groupId"), map->((Long)map.get("total")).intValue()));
	}

	@Override
	public Integer selectHostTotal() {
		return infraMapper.selectHostTotal();
	}
	
}
