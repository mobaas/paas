/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mobaas.paas.JsonResult;
import com.mobaas.paas.PageList;
import com.mobaas.paas.model.AppAction;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.model.Docker;
import com.mobaas.paas.dao.AppMapper;
import com.mobaas.paas.service.AppService;

@Service
public class AppServiceImpl implements AppService {

	@Resource
	protected AppMapper mapper;

	@Override
	public AppInfo selectAppInfoById(String appId) {

		return mapper.selectAppInfoById(appId);
	}
	
	@Override
	public void insertAppInfo(AppInfo ai) {
		mapper.insertAppInfo(ai);
	}
	
	@Override
	public int updateAppInfo(AppInfo ai) {
		return mapper.updateAppInfo(ai);
	}
	
	@Override
	public PageList<AppInfo> selectAppInfoList(String appId, int pageNo, int pageSize) {
		PageList<AppInfo> plist = new PageList<>(pageNo, pageSize);
		plist.setTotal(mapper.selectAppInfoCount( appId));
		if (plist.getTotal() > 0) {
			plist.setList( mapper.selectAppInfoList( appId, plist.getOffset(), pageSize) );
		}
		
		return plist;
	}

	@Override
	public int selectAppInfoTotal() {
		return mapper.selectAppInfoTotal();
	}
	
	@Override
	public AppVersion selectAppVersionByVersion(String productNo, String version) {
		return mapper.selectAppVersionByVersion(productNo, version);
	}

	@Override
	public AppVersion selectAppVersionById(int id) {
		return mapper.selectAppVersionById(id);
	}
	
	@Override
	public PageList<AppVersion> selectAppVersionList(String productNo, int pageNo, int pageSize) {
		PageList<AppVersion> plist = new PageList<>(pageNo, pageSize);
		plist.setTotal(mapper.selectAppVersionCount(productNo));
		if (plist.getTotal() > 0) {
			plist.setList( mapper.selectAppVersionList(productNo, plist.getOffset(), pageSize) );
		}
		
		return plist;
	}

	@Override
	public void insertAppVersion(AppVersion ver) {
		mapper.insertAppVersion(ver);
	}

	@Override
	public int deleteAppVersion(int id) {
		return mapper.deleteAppVersion(id);
	}
	
	@Override
	public AppVersion selectAppVersionLast(String productNo) {
		return mapper.selectAppVersionLast(productNo);
	}

	@Override
	public void insertAppAction(AppAction action) {
		mapper.insertAppAction(action);
	}

	@Override
	public int updateAppAction(AppAction action) {
		return mapper.updateAppAction(action);
	}

	@Override
	public PageList<AppAction> selectAppActionList(String appId, int pageNo, int pageSize) {
		PageList<AppAction> plist = new PageList<>(pageNo, pageSize);
		plist.setTotal(mapper.selectAppActionCount(appId));
		if (plist.getTotal() > 0) {
			plist.setList( mapper.selectAppActionList(appId, plist.getOffset(), pageSize) );
		}
		
		return plist;
	}

	@Override
	public Docker selectDockerByNo(String dockerNo) {
		return mapper.selectDockerByNo(dockerNo);
	}

}
