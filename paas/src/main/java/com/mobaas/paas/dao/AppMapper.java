/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mobaas.paas.JsonResult;
import com.mobaas.paas.model.AppAction;
import com.mobaas.paas.model.AppGrayVersion;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.model.Docker;

public interface AppMapper {

	AppInfo selectAppInfoByDomain(
			@Param("domain")String domain);
	
	AppInfo selectAppInfoById(
			@Param("appId")String appId);

	void insertAppInfo(AppInfo ai);

	int updateAppInfo(AppInfo ai);
	
	int selectAppInfoCount(
			@Param("appId")String appId);

	List<AppInfo> selectAppInfoList(
			@Param("appId")String appId,
			@Param("offset")int offset, 
			@Param("limit")int limit);

	int selectAppInfoTotal();

	AppVersion selectAppVersionByVersion(
			@Param("productNo")String productNo, 
			@Param("version")String version);

	AppVersion selectAppVersionById(
			@Param("id")int id);
	
	int selectAppVersionCount(
			@Param("productNo")String productNo);

	List<AppVersion> selectAppVersionList(
			@Param("productNo")String productNo, 
			@Param("offset")int offset, 
			@Param("limit")int limit);

	void insertAppVersion(AppVersion ver);

	int deleteAppVersion(int id);
	
	AppVersion selectAppVersionLast(
			@Param("productNo")String productNo);

	void insertAppAction(AppAction action);

	int updateAppAction(AppAction action);

	int selectAppActionCount(
			@Param("appId")String appId);

	List<AppAction> selectAppActionList(
			@Param("appId")String appId, 
			@Param("offset")int offset, 
			@Param("limit")int limit);

	Docker selectDockerByNo(
			@Param("dockerNo")String dockerNo);

	List<AppGrayVersion> selectAppGrayVersionList(
			@Param("appId")String appId);

	AppGrayVersion selectAppGrayVersionByVersion(
			@Param("appId")String appId, 
			@Param("version")String version);

	void insertAppGrayVersion(AppGrayVersion gver);

	int deleteAppGrayVersion(
			@Param("id")int id);

}
