/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;

@Component
public class KaptchaConfig {

	@Bean
	public Producer defaultKaptcha() {
		DefaultKaptcha kaptcha = new DefaultKaptcha(); 
		  Properties properties = new Properties(); 
		  properties.setProperty("kaptcha.border", "yes"); 
		  properties.setProperty("kaptcha.border.color", "105,179,90"); 
		  properties.setProperty("kaptcha.textproducer.font.color", "blue"); 
		  properties.setProperty("kaptcha.image.width", "90"); 
		  properties.setProperty("kaptcha.image.height", "30"); 
		  properties.setProperty("kaptcha.textproducer.font.size", "24"); 
		  properties.setProperty("kaptcha.session.key", "code"); 
		  properties.setProperty("kaptcha.textproducer.char.length", "4"); 
		  //properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑"); 
		  Config config = new Config(properties); 
		  kaptcha.setConfig(config); 
		  return kaptcha; 
	}
}
