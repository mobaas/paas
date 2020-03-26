/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.model;

import java.util.Date;

//后台人员
public class Admin {

    private int id;

    private String username;//用户名

    private String password;//登陆密码

    private String loginIp;//登陆ip

    private Date loginTime;//登陆时间

    private int state; // 状态
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getState() {
    	return state;
    }
    
    public void setState(int state) {
    	this.state = state;
    }
    
}