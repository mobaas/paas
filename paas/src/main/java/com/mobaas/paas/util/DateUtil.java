/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static int getYear() {
		
		return getYear(new Date());
	}
	
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static String toStringNoSep(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		return fmt.format(date);
	}
	
	public static String toDateStringNoSep(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(date);
	}
	
	public static String toDateTimeString(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return fmt.format(date);
	}
	
	public static String toDateString(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		return fmt.format(date);
	}
	
	public static String toMmDdString(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat fmt = new SimpleDateFormat("MM-dd");
		return fmt.format(date);
	}
	
	public static String toHhMmString(Date date) {
		if (date == null)
			return "";

		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
		return fmt.format(date);
	}
	
	public static String toUTCString(Date date) {
		
		if (date == null)
			return "";

		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return fmt.format(date);
	}
}
