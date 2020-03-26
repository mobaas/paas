/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.interceptor.ManageInterceptor;

/*
 * author: billy zhang
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Autowired
	private PaasConfig config;
	
	@Bean
	ManageInterceptor manageInterceptor() {
		return new ManageInterceptor();
	}
	
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**").addResourceLocations("file:" + config.getUploadPath());
	}

	/**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(manageInterceptor())
        			.addPathPatterns("/**")
        			.excludePathPatterns("/login", "/captcha")
        			.excludePathPatterns("/file/**")
        			.excludePathPatterns("/upload/**")
        			.excludePathPatterns("/lib/**", "/kindeditor/**", "/ossupload/**", "/webuploader/**");

    }

}
