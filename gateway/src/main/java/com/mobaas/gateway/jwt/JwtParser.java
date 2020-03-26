/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.gateway.jwt;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

public class JwtParser {

    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";
    
    private static final String PERMISSION_TOKEN_EXPIRED = "token expired.";
    
    private static final String PERMISSION_TOKEN_INVALID = "token invalid.";
    
    /**
                 *  解析jwt
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .setAllowedClockSkewSeconds(180)
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (ExpiredJwtException  eje) {
            throw new RuntimeException(PERMISSION_TOKEN_EXPIRED, eje);
        } catch (Exception e){
            throw new RuntimeException(PERMISSION_TOKEN_INVALID, e);
        }
    }

    /**
     * 从token中获取用户名
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUsername(String token, String base64Security){
        return parseJWT(token, base64Security).getSubject();
    }

    /**
     * 从token中获取用户ID
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUserId(String token, String base64Security){
        String userId = parseJWT(token, base64Security).get("userId", String.class);
        return new String(Base64.getDecoder().decode(userId), Charset.forName("utf-8"));
    }

    /**
     * 是否已过期
     * @param token
     * @param base64Security
     * @return
     */
    public static boolean isExpiration(String token, String base64Security) {
        return parseJWT(token, base64Security).getExpiration().before(new Date());
    }
}
