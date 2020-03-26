/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobaas.paas.dao.AdminMapper;
import com.mobaas.paas.model.Admin;
import com.mobaas.paas.service.AdminService;
import com.mobaas.paas.PageList;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminMapper adminMapper;

	@Override
	public Admin selectAdminByName(String username) {

	    return adminMapper.selectAdminByName(username);
	    
	}

	@Override
	public Admin selectAdminById(int adminId) {
		return adminMapper.selectAdminById(adminId);
	}

	@Override
	public PageList<Admin> selectAdminList(int pageNo, int pageSize) {
		
		PageList<Admin> pi = new PageList<>(pageNo, pageSize);
		pi.setTotal( adminMapper.selectAdminCount() );
		pi.setList( adminMapper.selectAdminList( pi.getOffset(), pageSize) );
		
		return pi;
	}

	@Override
	public void insertAdmin(Admin adm) {
		adminMapper.insertAdmin(adm);
	}

	@Override
	public int updateAdmin(Admin adm) {
		return adminMapper.updateAdmin(adm);
	}
	
	@Override
	public int deleteAdmin(int admId) {

		return adminMapper.deleteAdmin(admId);

	}
	
	@Override
	public int selectAdminCountByName(String name) {
		return adminMapper.selectAdminCountByName(name);
	}
	
}
