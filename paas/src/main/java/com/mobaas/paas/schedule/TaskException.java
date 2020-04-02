/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.schedule;

/**
 * 
 * @author billy zhang
 * 
 */
public class TaskException extends RuntimeException {

	public TaskException(String message, Exception ex) {
		super(message, ex);
	}

	
}
