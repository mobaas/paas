/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas;

import org.springframework.context.ApplicationContext;

public class AppContextUtil {

	private static ApplicationContext context;
	
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	public static ApplicationContext getAppContext() {
		return context;
	}
	
	public static void setAppContext(ApplicationContext context) {
		AppContextUtil.context = context;
	}

}
