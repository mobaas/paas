/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.util.CryptoUtil;

@Controller
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	public ObjectMapper jsonMapper;
	@Autowired
	public PaasConfig config;

	/**
	 * receive upload file for webuploader
	 * save file to local.
	 * @param upfile
	 * @param cate
	 * @return
	 */
	@RequestMapping(value="upload")
    @ResponseBody
    public String upload(
    		@RequestParam("file")MultipartFile upfile,
    		@RequestParam("cate")String cate) {
		
		try {
			String fname = upfile.getOriginalFilename().toLowerCase();
			
			String suffix = "";
			if (fname.lastIndexOf(".") > 1) {
				suffix = fname.substring(fname.lastIndexOf(".") + 1);
			}
			
			if ("doc".equals( suffix ) ||
					"docx".equals( suffix ) ||
					"pdf".equals( suffix ) ||
					"xls".equals( suffix ) ||
					"xlsx".equals( suffix ) ||
					"ppt".equals( suffix ) ||
					"pptx".equals( suffix ) ||
					"jpg".equals( suffix ) ||
					"png".equals( suffix ) ||
					"gif".equals( suffix ) ||
					"bmp".equals( suffix ) ||
					"jpeg".equals( suffix ) ||
					"jar".equals( suffix ) ||
					"war".equals( suffix ) ||
					"zip".equals( suffix )
					) {
				
		        String fileName = CryptoUtil.MD5(System.currentTimeMillis()+"_"+fname) + "." + suffix;
	
		        String objectName = cate + "/" + fileName;   

		        Path path = Paths.get(config.getUploadPath(), objectName);
		        if (!Files.exists(path.getParent())) {
		        		Files.createDirectory(path.getParent());
		        }
				Files.copy(upfile.getInputStream(), path); 
		      
		        String resp = "{\"msg\":\"success\", \"fileUrl\":\"/upload/" + objectName + "\", \"fileName\":\"" + upfile.getOriginalFilename() + "\", \"fileSize\":" + upfile.getSize() + " }";
		        System.out.println(resp);
		        return resp;
			} else {

				return "{\"msg\":\"文件格式不支持。\"}";
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			
			return "{\"msg\":\"error\"}";
		}
    }
	
}

