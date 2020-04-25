package com.mobaas.paas.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobaas.paas.JsonResult;
import com.mobaas.paas.model.AccessLog;
import com.mobaas.paas.model.AccessTotal;
import com.mobaas.paas.service.AccessLogService;


@RestController
public class AccessLogController {

	@Autowired
	private AccessLogService service;

	private static ExecutorService es = Executors.newCachedThreadPool();

	@GetMapping("accesstotal/listbydate")
	public JsonResult<List<AccessTotal>> accessTotalListByDate(
			@RequestParam("date")String date,
			@RequestParam("pageno")int pageNo,
			@RequestParam("pagesize")int pageSize) {
		
		int offset = (pageNo -1) * pageSize;
		List<AccessTotal> list = service.selectAccessTotalListByDate(date, offset, pageSize);
		return JsonResult.ok(list);
	}
	
	@GetMapping("accesstotal/listbyservice")
	public JsonResult<List<AccessTotal>> accessTotalListByService(
			@RequestParam("service")String serv,
			@RequestParam("start")String start,
			@RequestParam("interval")int interval) {
		
		List<AccessTotal> list = service.selectAccessTotalListByService(serv, start, interval);
		return JsonResult.ok(list);
	}

	@GetMapping("accesstotal/last")
	public JsonResult<AccessTotal> accessTotalLast(
			@RequestParam("interval")int interval) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, 0 - interval - 300);
		
		AccessTotal total = service.selectAccessTotalLast(cal.getTime(), interval);
		return JsonResult.ok(total);
	}
	
	@PostMapping("accesslog/save")
	public JsonResult<Integer> accessLogSave(
			@RequestBody List<AccessLog> loglist) {
		
		es.execute(()-> {
			
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
			
			for (AccessLog alog : loglist) {
				
				try {
					alog.setPath( URLDecoder.decode( alog.getPath(), "utf-8") );//"uri":"\/lib\/102589_1.aspx",
				} catch (UnsupportedEncodingException ex) {
				}
				
				try {
					String[] strs = alog.getTimeLocal().split(" ");
					alog.setTime( fmt.parse(strs[0]) ); //"timeLocal":"04\/Mar\/2018:22:41:35 +0800",
				} catch (ParseException ex) {
					//log.warn(ex.getLocalizedMessage());
					alog.setTime(new Date());
				} 
				
				service.insertAccessLog(alog);
			}
		});
		
		return JsonResult.ok(0);
	}
	
}
