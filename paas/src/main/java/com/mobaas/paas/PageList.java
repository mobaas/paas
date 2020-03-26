/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PageList<T> {

	private int pageNo;
	private int pageSize;
	private int total;
	private List<T> list;
	
	public PageList() { 
		
	}
	
	public PageList(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getList() {
		return list != null ? list : new ArrayList<T>();
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	@JsonIgnore
	public int getOffset() {
		if (pageNo < 1)
			return 0;
		
		return (pageNo -1 ) * pageSize;
	}
	
}
