/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.notifier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.mobaas.paas.Notifier;

public class DingTalkNotifier implements Notifier {

	private final String webhookApi = "https://oapi.dingtalk.com/robot/send?access_token=";
	
    private RestTemplate restTemplate = new RestTemplate();
    private String atMobiles;
    private String msgtype = "markdown";
    private String sign = "【监控通知】";

    public void notify(String title, String text, Map<String, Object> config) {
    	String accessToken = (String)config.get("accessToken");
    	String webhookUrl = webhookApi + accessToken;
    	restTemplate.postForEntity(webhookUrl, createMessage(title+sign, text), Void.class);
    }
    
    private HttpEntity<Map<String, Object>> createMessage(String title, String text) {
        Map<String, Object> messageJson = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("text", text);
        params.put("title", title);
        //messageJson.put("dinggroup", this.dingGroup);
        messageJson.put("atMobiles", this.atMobiles);
        messageJson.put("msgtype", this.msgtype);
        messageJson.put(this.msgtype, params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(messageJson, headers);
    }

    private String getAtMobilesString(String s) {
        StringBuilder atMobiles = new StringBuilder();
        String[] mobiles = s.split(",");
        for (String mobile : mobiles) {
            atMobiles.append("@").append(mobile);
        }
        return atMobiles.toString();
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(String atMobiles) {
        this.atMobiles = atMobiles;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

}