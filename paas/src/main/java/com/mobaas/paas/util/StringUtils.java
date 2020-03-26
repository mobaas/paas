/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.util;

import java.util.Random;

public final class StringUtils {

	private static final String NUMBER = "123456789";
	private static final String ALPHA_NUMBER = "abcdefg123hijklmn456opqrst789uvwxyz0";
	
	public static String getRandomNumber(int len) {
		
		Random rnd = new Random();
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<len; i++) {
			sb.append(NUMBER.charAt(rnd.nextInt(NUMBER.length())));
		}
		
		return sb.toString();
	}
	
	public static String getRandomString(int len) {
		
		Random rnd = new Random();
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<len; i++) {
			sb.append(ALPHA_NUMBER.charAt(rnd.nextInt(ALPHA_NUMBER.length())));
		}
		
		return sb.toString();
	}
	
}
