/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan({"com.mobaas.paas.dao"})
@Configuration
public class MyBatisConfig {

}
