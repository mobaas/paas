/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas;

public class JsonResult<T> {
	
	private JsonResult() {
		
	}
	
	private int errCode;
	private String errMsg;
	private T data;
	
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public static <T> JsonResult<T> ok(T data) {
		JsonResult<T> jr = new JsonResult<T>();
		jr.setData(data);
		return jr;
	}
	
	public static <T> JsonResult<T> fail(int errCode, String errMsg) {
		JsonResult<T> jr = new JsonResult<T>();
		jr.setErrCode(errCode);
		jr.setErrMsg(errMsg);
		return jr;
	}

	public static <T> JsonResult<T> fail(int errCode, String errMsg, T data) {
		JsonResult<T> jr = new JsonResult<T>();
		jr.setErrCode(errCode);
		jr.setErrMsg(errMsg);
		jr.setData(data);
		return jr;
	}
	
}
