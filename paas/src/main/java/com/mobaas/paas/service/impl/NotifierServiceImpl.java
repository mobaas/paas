/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobaas.paas.PageList;
import com.mobaas.paas.ComponentFactory;
import com.mobaas.paas.dao.NotifierMapper;
import com.mobaas.paas.model.Notification;
import com.mobaas.paas.service.NotifierService;
import com.mobaas.paas.Notifier;

/**
 * 
 * @author billy zhang
 * 
 */
@Service
public class NotifierServiceImpl implements NotifierService {

	@Autowired
	private NotifierMapper notiMapper;
	
	@Autowired
	private ComponentFactory<Notifier> notifierFactory;
	
	@Autowired
	private ObjectMapper jsonMapper;
	
	@Override
	public void notify(String target, String title, String text) {
		List<Notification> list = notiMapper.selectNotificationListByTarget(target);
		for (Notification noti : list) {
			Notifier notifier = notifierFactory.getComponent(noti.getNotifier());
			try {
				Map<String, Object> config = jsonMapper.readValue(noti.getConfig(), Map.class);
				notifier.notify(title, text, config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Notification selectNotificationById(int id) {
		return notiMapper.selectNotificationById(id);
	}

	@Override
	public PageList<Notification> selectNotificationList(int pageNo, int pageSize) {
		PageList<Notification> pglist = new PageList<>(pageNo, pageSize);
		pglist.setTotal( notiMapper.selectNotificationCount() );
		if (pglist.getTotal() > 0) {
			pglist.setList( notiMapper.selectNotificationList(pglist.getOffset(), pageSize) );
		}
		return pglist;
	}

}
