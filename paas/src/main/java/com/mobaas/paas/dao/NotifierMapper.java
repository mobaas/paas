package com.mobaas.paas.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mobaas.paas.model.Notification;

/**
 * 
 * @author billy zhang
 * 
 */
public interface NotifierMapper {

	List<Notification> selectNotificationListByTarget(
			@Param("target")String target);

	Notification selectNotificationById(
			@Param("id")int id);

	int selectNotificationCount();

	List<Notification> selectNotificationList(
			@Param("offset")int offset, 
			@Param("limit")int limit);
}
