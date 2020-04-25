package com.mobaas.paas.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mobaas.paas.JsonResult;
import com.mobaas.paas.PageList;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.service.AppService;
import com.mobaas.paas.service.InstanceService;

@Controller
@RequestMapping("/devops")
public class DevOpsController {

	@Autowired
	private AppService appService;
	@Autowired
	private InstanceService instService;
    @Autowired
    private PaasConfig config;
    
	/*
	 * Api服务帐号列表页面
	 */
	@GetMapping(value = "build")
	@ResponseBody
    public JsonResult<Integer> build(
			@RequestParam(value = "appid", defaultValue = "", required = false) String appId) {
        
		AppInfo ai = appService.selectAppInfoById(appId);
		if (ai == null) {
			return JsonResult.fail(-1, "invalid appid.");
		}
		
		PageList<AppVersion> pglist = appService.selectAppVersionList(appId, 1, 1000);
		
		List<String> existlist = pglist.getList()
				.stream()
				.map((ver)->{ return ver.getFilename(); })
				.collect(Collectors.toList());
		
		int n=0;
		Path buildPath = Paths.get( config.getBuildPath(), appId );
		String[] filelist = buildPath.toFile().list();
		for (String filename : filelist) {
			
			if (!existlist.contains(filename)) {
				
				String filename2 = filename.substring(0, filename.lastIndexOf('.'));
				String version = filename2.substring(filename2.lastIndexOf('-') + 1);
				
				AppVersion ver = new AppVersion();
				ver.setAppId(ai.getAppId());
				ver.setVersion(version);
				ver.setFilename(filename);
				ver.setChangelog("build.");
				
				ver.setFilePath( Paths.get( config.getBuildPath(), appId, filename ).toString() );
				ver.setAddTime(new Date());
				appService.insertAppVersion(ver);
				
				if (ai.getAutoDeploy() == 1) {
					instService.deploy(ai, ver);
				}
				
				++n;
			}
		}
		
        return JsonResult.ok(n);
    }
	 
	
}
