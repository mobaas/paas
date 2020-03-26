/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Producer;
import com.mobaas.paas.JsonResult;
import com.mobaas.paas.Constants;
import com.mobaas.paas.model.Admin;
import com.mobaas.paas.service.AdminService;
import com.mobaas.paas.service.AppService;
import com.mobaas.paas.util.CryptoUtil;

@Controller
public class HomeController extends BaseController {
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private AppService appService;
	@Autowired
	private Producer captchaProducer;
	
	@GetMapping(value = "/")
    public String home(){
        return "forward:/index";
    }

    @RequestMapping(value = "/index")
    public ModelAndView index(){

    	ModelAndView mv = new ModelAndView();
    	
    	mv.setViewName("/index");
        return mv;
    }

    @RequestMapping(value = "/denied")
    public ModelAndView denied(){

        return new ModelAndView("/denied");
    }

    @GetMapping(value = "/login")
    public ModelAndView login(){

        return new ModelAndView("/login");
    }


    @PostMapping(value = "/login")
    @ResponseBody
    public JsonResult<Integer> loginPost(
    		@RequestParam("username")String username,
    		@RequestParam("password")String password,
    		@RequestParam("vcode")String vcode,
    		HttpServletRequest request) throws Exception {
    	
    	    String captcha = (String)request.getSession().getAttribute("captcha");
		if (!vcode.equalsIgnoreCase(captcha)) {
			return JsonResult.fail(-2, "验证码不正确。");
		}
    	
    	 	// 检验数据有效性
	    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
	        return JsonResult.fail(-1, "请输入用户名和密码。");
	    }

	    Admin admin = adminService.selectAdminByName(username);
	    if (admin == null) {
	    	return JsonResult.fail(-1, "用户名不存在。");
	    }
	    
	    if (admin.getState() != 0) {
	    	return JsonResult.fail(-1, "帐户已禁用");
	    }
	    
	    // zh3Ad7inn
	    if (!admin.getPassword().equalsIgnoreCase(CryptoUtil.MD5(password))) {
	    	return JsonResult.fail(-1, "密码错误。");
	    }
	    
	    admin.setLoginIp(getRequest().getRemoteAddr());
	    admin.setLoginTime(new Date());
	    adminService.updateAdmin(admin);
	    
	    Cookie cookie = new Cookie(Constants.ADMIN_COOKIEID, CryptoUtil.encryptDES(Constants.ADMIN_KEY, admin.getId() + ""));
		getResponse().addCookie(cookie);
		
		return JsonResult.ok(0);

    }
    
    @GetMapping(value = "/logout")
    @ResponseBody
    public ModelAndView logout(
    		HttpServletRequest request,
    		HttpServletResponse response) {

    	request.getSession().invalidate();

    	 Cookie[] cookies = request.getCookies();
         if(null!=cookies){
             for(Cookie cookie : cookies){
                 if(Constants.ADMIN_COOKIEID.equals(cookie.getName())){
                	 cookie.setValue(null);
                 	cookie.setMaxAge(-1);
                 	cookie.setPath("/");
                 	response.addCookie(cookie);
                 	break;
                 }
             }
         }

    	ModelAndView model=new ModelAndView();
    	model.setViewName("redirect:/login");
    	return model;

    }

	@GetMapping(value="changepwd")
    public ModelAndView changePwd() {
    	ModelAndView model=new ModelAndView();

        model.setViewName("changepwd");

        return model;
    }

	@PostMapping(value="changepwd")
	@ResponseBody
    public JsonResult<Integer> changePwdPost(
    		@RequestParam("passwd")String passwd,
    		@RequestParam("passwd2")String passwd2,
    		HttpServletRequest request) {

    	Admin adm = (Admin)request.getSession().getAttribute(Constants.ADMIN_SESSIONID);
    	if (adm.getPassword().equalsIgnoreCase(CryptoUtil.MD5(passwd))) {
    		adm.setPassword(CryptoUtil.MD5(passwd2));
    		adminService.updateAdmin(adm);

            return JsonResult.ok(0);
    	} else {
            return JsonResult.fail(-1, "原密码不正确。");
    	}
    }

	@RequestMapping("/captcha") 
    public void captcha(HttpServletRequest request,
    		HttpServletResponse response) throws Exception{ 
      byte[] captchaChallengeAsJpeg = null; 
       ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream(); 
       try { 
	       //生产验证码字符串并保存到session中 
	       String createText = captchaProducer.createText(); 
	       request.getSession().setAttribute("captcha", createText); 
	       //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中 
	       BufferedImage challenge = captchaProducer.createImage(createText); 
	       ImageIO.write(challenge, "jpg", jpegOutputStream); 
       } catch (IllegalArgumentException e) { 
	        response.sendError(HttpServletResponse.SC_NOT_FOUND); 
	        return; 
       } 
       //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组 
       captchaChallengeAsJpeg = jpegOutputStream.toByteArray(); 
       response.setHeader("Cache-Control", "no-store"); 
       response.setHeader("Pragma", "no-cache"); 
       response.setDateHeader("Expires", 0); 
       response.setContentType("image/jpeg"); 
       
       try (ServletOutputStream responseOutputStream = response.getOutputStream()) { 
	       responseOutputStream.write(captchaChallengeAsJpeg); 
	       responseOutputStream.flush(); 
       }
    }
}
