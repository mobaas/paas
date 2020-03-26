/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mobaas.paas.AdminAuth;
import com.mobaas.paas.Constants;
import com.mobaas.paas.model.Admin;
import com.mobaas.paas.service.AdminService;
import com.mobaas.paas.util.CryptoUtil;

/*
 * author: billy zhang
 */
public class ManageInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    //在请求处理之前进行调用（Controller方法调用之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        request.setAttribute("path", request.getRequestURI());

        String path = request.getRequestURI().substring(1);

        int n = path.indexOf("/");
        String module = n > 0 ? path.substring(0, n) : "index";
        request.setAttribute("module", module);

        Admin loginAdmin = (Admin) request.getSession().getAttribute(Constants.ADMIN_SESSIONID);

        if (loginAdmin == null) {

            String adminstr = "";

            Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if (Constants.ADMIN_COOKIEID.equals(cookie.getName())) {
                        adminstr = cookie.getValue();
                        if (adminstr != null)
                            break;
                    }
                }

                if (!StringUtils.isEmpty(adminstr)) {
                    int adminId = Integer.parseInt(CryptoUtil.decryptDES(Constants.ADMIN_KEY, adminstr));
                    loginAdmin = adminService.selectAdminById(adminId);
                }
            }
            if (loginAdmin != null) {

                request.getSession().setAttribute(Constants.ADMIN_SESSIONID, loginAdmin);

                return goOn(loginAdmin, module, request, response);

            } else {
                response.sendRedirect("/login");
                return false;
            }
        } else {

            return goOn(loginAdmin, module, request, response);
        }
    }

    private boolean goOn(Admin admin, String module,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        AdminAuth auth = new AdminAuth();
        auth.setAdmin(admin);
        request.setAttribute(Constants.ADMIN_AUTH, auth);

        return true;
    }
}
