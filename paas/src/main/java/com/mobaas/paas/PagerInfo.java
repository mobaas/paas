/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas;

/**
 **/
public class PagerInfo {
    // --分页参数 --//
    /**
     * 页编号 : 第几页
     */
    private int pageNo = 1;
    /**
     * 页大小 : 每页的数量
     */
    private int pageSize = 20;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 总页数
     */
    private int totalPages;
    
  
    private String baseUrl;
    
    public PagerInfo(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    // -- 访问查询参数函数 --//
    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 获得每页的记录数量,默认为1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 取得总记录数, 默认值为-1.
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
        this.totalPages = this.getTotalPages();
    }

    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public int getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }
        int pages = totalCount / pageSize;
        return totalCount % pageSize > 0 ? ++pages : pages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public String getBaseUrl() {
    	return this.baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
    	this.baseUrl = baseUrl;
    }
    
    public boolean hasPrev() {
    	return pageNo > 1;
    }
    
    public boolean hasNext() {
    	return pageNo < totalPages;
    }
    
    public int getBegin() {
    	return (pageNo-1) / 10 * 10 + 1;
    }
    
    public int getEnd() {
    	int end = getBegin() + 10;
    	if (end > totalPages)
    		end = totalPages;
    	return end;
    }
    
    public String getFirstUrl() {
    	return getPageUrl(1);
    }
    
    public String getPrevUrl() {
    	return getPageUrl(pageNo-1);
    }
    
    public String getNextUrl() {
    	return getPageUrl(pageNo+1);
    }
    
    public String getLastUrl() {
    	return getPageUrl(totalPages);
    }
    
    public String getPageUrl(int page) {
	    	if (page < 1 || page > totalPages) 
	    		return "javascript:void(0);";
    	
	    	if (baseUrl.indexOf("?") == -1)
	    		return baseUrl + page;
	    	else
	    		return baseUrl + "&pageno=" + page;
    	
    }
    
    public String getFirstUrl2() {
    	return getPageUrl2(1);
    }
    
    public String getPrevUrl2() {
    	return getPageUrl2(pageNo-1);
    }
    
    public String getNextUrl2() {
    	return getPageUrl2(pageNo+1);
    }
    
    public String getLastUrl2() {
    	return getPageUrl2(totalPages);
    }
    
    public String getPageUrl2(int page) {
    	if (page < 1 || page > totalPages) 
    		return "javascript:void(0);";
	
    	if (baseUrl.indexOf("?") == -1)
    		return baseUrl + "?pageno=" + page;
    	else
    		return baseUrl + "&pageno=" + page;
	
}
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Page{");
        sb.append("pageNo=").append(pageNo);
        sb.append(", totalPages=").append(totalPages);
        sb.append(", totalCount=").append(totalCount);
        sb.append(", pageSize=").append(pageSize);
        sb.append('}');
        return sb.toString();
    }
}
