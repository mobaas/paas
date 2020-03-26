/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service;

import com.mobaas.paas.PageList;
import com.mobaas.paas.model.Admin;

public interface AdminService {

	Admin selectAdminByName(String username);
	
	Admin selectAdminById(int adminId);
	
	PageList<Admin> selectAdminList(int pageNum, int pageSize);

	void insertAdmin(Admin adm);
	
	int updateAdmin(Admin adm);

	int selectAdminCountByName(String name);

	int deleteAdmin(int admId);
}
