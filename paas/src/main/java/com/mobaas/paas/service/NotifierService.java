package com.mobaas.paas.service;

import com.mobaas.paas.PageList;
import com.mobaas.paas.model.Notification;

public interface NotifierService {

	void notify(String target, String title, String text);

	Notification selectNotificationById(int id);

	PageList<Notification> selectNotificationList(int pageNo, int pageSize);

}
