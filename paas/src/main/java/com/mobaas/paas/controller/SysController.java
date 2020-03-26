/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.controller;

import java.util.Collections;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mobaas.paas.JsonResult;
import com.mobaas.paas.PageList;
import com.mobaas.paas.PagerInfo;
import com.mobaas.paas.model.Admin;
import com.mobaas.paas.service.AdminService;
import com.mobaas.paas.util.CryptoUtil;

@Controller
@RequestMapping(value = "/sys")
public class SysController extends BaseController {
	
    @Autowired
    private AdminService adminService;

    @GetMapping(value = "adminlist")
    public ModelAndView adminList(
    		@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView model = new ModelAndView();

        PagerInfo pager = new PagerInfo(pageNo, pageSize);
        PageList<Admin> plist = adminService.selectAdminList( pageNo, pageSize);

        model.addObject("list", plist.getList());

        pager.setTotalCount(plist.getTotal());

        model.addObject("pager", pager);
        model.setViewName("sys/adminlist");

        return model;
    }

    @GetMapping(value = "adminadd")
    public ModelAndView adminAdd() {
        ModelAndView model = new ModelAndView();

        model.setViewName("sys/adminadd");

        return model;
    }

    @PostMapping(value = "adminadd")
    @ResponseBody
    public JsonResult<Integer> adminAddPost(
            @RequestParam("name") String name,
            @RequestParam("passwd") String passwd,
            @RequestParam("state") int state) {

    	int n = adminService.selectAdminCountByName(name);
        if (n == 0) {
            Admin adm = new Admin();
            adm.setUsername(name);
            adm.setPassword(CryptoUtil.MD5(passwd));
            adm.setState(state);

            adminService.insertAdmin(adm);

            return JsonResult.ok(0);
        } else {
            return JsonResult.fail(-1, "用户名已存在。");
        }
    }

    @GetMapping(value = "adminedit")
    public ModelAndView adminEdit(
            @RequestParam("id") int adminId) {
        ModelAndView model = new ModelAndView();

        Admin admin = adminService.selectAdminById(adminId);
        
        model.addObject("admin", admin);

        model.setViewName("sys/adminedit");

        return model;
    }

    @PostMapping(value = "adminedit")
    @ResponseBody
    public JsonResult<Integer> adminEditPost(
            @RequestParam("id") int adminId,
            @RequestParam("passwd") String passwd,
            @RequestParam("state") int state) {

    	Admin adm = adminService.selectAdminById(adminId);
        if (!"".equals(passwd)) {
            adm.setPassword(CryptoUtil.MD5(passwd));
        }
        adm.setState(state);

        int n = adminService.updateAdmin(adm);
        return JsonResult.ok(n);
    }

    @PostMapping(value="admindelete")
    @ResponseBody
    public JsonResult<Integer> adminDeletePost(@RequestParam(value="id")int id) {
    	
    	int n = adminService.deleteAdmin(id);
    	return JsonResult.ok(n);
    	
    }
    
}
