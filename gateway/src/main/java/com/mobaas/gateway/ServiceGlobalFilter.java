/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.gateway;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.mobaas.gateway.jwt.JwtConstant;
import com.mobaas.gateway.jwt.JwtParser;

import reactor.core.publisher.Mono;

@Component
public class ServiceGlobalFilter implements GlobalFilter, Ordered {

	private static final Logger log = LoggerFactory.getLogger(ServiceGlobalFilter.class);
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			final String authHeader = exchange.getRequest().getHeaders().getFirst(JwtConstant.AUTH_HEADER_KEY);
			
			if (checkJwtHeader(authHeader)) {
				
				String path = exchange.getRequest().getURI().getPath();
				String[] strs = path.split("/");
				String serviceName = strs[1];
				
				// do logic
				
			} else {
				return error(exchange, -1, "JWT invalid");
			}
			
			
			return chain.filter(exchange);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			
			return error(exchange, 1005, ex.getLocalizedMessage());
		}
	}

	private boolean checkJwtHeader(final String jwtHeader) {
		
        final String token = jwtHeader.substring(JwtConstant.TOKEN_PREFIX.length());

		log.info("token: " + token);
		
        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        JwtParser.parseJWT(token, JwtConstant.BASE64_SECRET);
        
        return true;
	}
	
	private Mono<Void> error(ServerWebExchange exchange, int errCode, String errMsg) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.BAD_REQUEST);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

		byte[] bytes = String.format("{\"errCode\":%s, \"errMsg\":\"%s\"}", errCode, errMsg).getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bytes);
		
		return response.writeWith(Mono.just(buffer));
	}
}
