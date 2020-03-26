/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mobaas.paas.model.Admin;

@Repository
public interface AdminMapper {
	
    int deleteAdmin(int id);

    int insertAdmin(Admin admin);

    Admin selectAdminById(int id);

    int updateAdmin(Admin admin);

	Admin selectAdminByName(
			@Param("username")String username);

	int selectAdminCount();

	List<Admin> selectAdminList(
			@Param("offset")int offset, 
			@Param("limit")int limit);

	int selectAdminCountByName(
			@Param("username")String username);

}