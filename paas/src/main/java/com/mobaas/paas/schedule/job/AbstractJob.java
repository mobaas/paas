/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.schedule.job;

import java.util.Map;

/**
 * 
 * @author billy zhang
 * 
 */
public abstract class AbstractJob {

	public final void execute(Map<String, Object> jobData) {
		
		try {
			doExecute(jobData);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected abstract void doExecute(Map<String, Object> jobData);
}
