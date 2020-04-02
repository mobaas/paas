package com.mobaas.paas.notifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobaas.paas.Notifier;

/**
 * 钉钉机器人通知
 * @author billy zhang
 *
 */
public class DingTalkNotifier implements Notifier {
	
	private final String webhookApi = "https://oapi.dingtalk.com/robot/send?access_token=";
	
    private String atMobiles;
    private String msgtype = "markdown";
    private String sign = "【监控通知】";
    
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper jsonMapper;
    
    public void notify(String title, String text, Map<String, Object> config) {
		String accessToken = (String)config.get("accessToken");
		
		Map<String, Object> message = createMessage(title+sign, text);
		
		try {
			String jsonStr = jsonMapper.writeValueAsString(message);
			restTemplate.postForEntity(webhookApi + accessToken, jsonStr, String.class);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
    }
    
    private Map<String, Object> createMessage(String title, String text) {
        Map<String, Object> messageJson = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("text", text);
        params.put("title", title);
        //messageJson.put("dinggroup", this.dingGroup);
        messageJson.put("atMobiles", this.atMobiles);
        messageJson.put("msgtype", this.msgtype);
        messageJson.put(this.msgtype, params);
        return messageJson;
    }

    private String getAtMobilesString(String s) {
        StringBuilder atMobiles = new StringBuilder();
        String[] mobiles = s.split(",");
        for (String mobile : mobiles) {
            atMobiles.append("@").append(mobile);
        }
        return atMobiles.toString();
    }

}
