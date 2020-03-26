/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas;

import com.mobaas.paas.model.Admin;

/*
 * 帮助类，在FTL页面内使用
 * author: billy zhang
 */
public class AdminAuth {

    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getAdminName() {
        return admin != null ? admin.getUsername() : "管理中心";
    }

    public boolean isManager() {
    	if (admin == null)
    		return false;
    	
    	return true;
    }
    
    /*
     * 获取当前用户的认证状态
     */
    public boolean hasRole(String role) {
    	if (admin == null)
    		return false;
    	
        return true;
    }

}
