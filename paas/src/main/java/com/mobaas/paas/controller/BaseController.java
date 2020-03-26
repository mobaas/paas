/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.mobaas.paas.Constants;
import com.mobaas.paas.model.Admin;

/*
 * author: billy zhang
 */
public class BaseController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;
	
	protected Admin getAdmin() {
		return (Admin)request.getSession().getAttribute(Constants.ADMIN_SESSIONID);
	}
	
	protected HttpServletRequest getRequest() {
		return request;
	}
	
	protected HttpServletResponse getResponse() {
		return response;
	}
	
}
