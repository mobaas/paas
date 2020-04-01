/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import java.util.Date;
import java.util.List;

import com.mobaas.paas.PageList;
import com.mobaas.paas.model.AppAction;
import com.mobaas.paas.model.AppGrayVersion;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.model.Docker;

public interface AppService {
	
	// AppInfo
	AppInfo selectAppInfoById(String appId);

	PageList<AppInfo> selectAppInfoList(String appId, int pageNo, int pageSize);

	void insertAppInfo(AppInfo ai);

	int updateAppInfo(AppInfo ai);

	int selectAppInfoTotal();
	
	// appversion
	AppVersion selectAppVersionByVersion(String appId, String version);

	AppVersion selectAppVersionById(int id);
	
	PageList<AppVersion> selectAppVersionList(String appId, int pageNo, int pageSize);

	void insertAppVersion(AppVersion ver);
	
	int deleteAppVersion(int id);
	
	AppVersion selectAppVersionLast(String appId);

	// appaction
	void insertAppAction(AppAction action);

	int updateAppAction(AppAction action);

	PageList<AppAction> selectAppActionList(String appId, int pageNo, int pageSize);

	// docker
	Docker selectDockerByNo(String dockerNo);

	// appgrayversion
	List<AppGrayVersion> selectAppGrayVersionList(String appId);

	AppGrayVersion selectAppGrayVersionByVersion(String appId, String version);

	void insertAppGrayVersion(AppGrayVersion gver);

	int deleteAppGrayVersion(int id);

}
