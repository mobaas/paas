/**
 * Copyright 2016-2018 mobaas.com
 */
package com.mobaas.paas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.mobaas.paas.JsonResult;
import com.mobaas.paas.PageList;
import com.mobaas.paas.PagerInfo;
import com.mobaas.paas.config.PaasConfig;
import com.mobaas.paas.deploy.Deployer;
import com.mobaas.paas.deploy.DeployerFactory;
import com.mobaas.paas.kubernetes.DeploymentPatchItem;
import com.mobaas.paas.model.Docker;
import com.mobaas.paas.service.AppService;
import com.mobaas.paas.service.InstanceService;
import com.mobaas.paas.service.KubeApiService;
import com.mobaas.paas.model.AppAction;
import com.mobaas.paas.model.AppEvent;
import com.mobaas.paas.model.AppInfo;
import com.mobaas.paas.model.AppInstance;
import com.mobaas.paas.model.AppVersion;
import com.mobaas.paas.util.DateUtil;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.models.ExtensionsV1beta1Ingress;
import io.kubernetes.client.models.V1Deployment;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.models.V1beta1Event;
import io.kubernetes.client.models.V1beta1EventList;

@Controller
@RequestMapping(value = "/app")
public class AppController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(AppController.class);
	
    @Autowired
    private AppService appService;
    @Autowired
    private InstanceService instService;
	@Autowired
	private KubeApiService kubeService;
	@Autowired
    private ApplicationContext appContext;
	@Autowired
	private DeployerFactory deployerFactory;
	@Autowired
	private PaasConfig config;
    
    @GetMapping(value = "applist")
    public ModelAndView appList(
    			@RequestParam(value = "appid", defaultValue = "", required = false) String appId,
    			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView model = new ModelAndView();

        PagerInfo pager = new PagerInfo(pageNo, pageSize);
        PageList<AppInfo> plist = appService.selectAppInfoList(appId, pageNo, pageSize);
            
        pager.setTotalCount(plist.getTotal());
        
        model.addObject("list", plist.getList());
       
        model.addObject("pager", pager);
        model.addObject("appid", appId);
        
        model.setViewName("app/applist");

        return model;
    }
    
    @GetMapping(value = "appdetail")
    public ModelAndView appDetail(
    		@RequestParam(value = "appid") String appId) {
        ModelAndView model = new ModelAndView();

		List<AppInstance> instlist = new ArrayList<>();
        AppInfo appInfo = appService.selectAppInfoById(appId);
		if (appInfo == null) {
			model.setViewName("notfound");
			return model;
		}
		
		try {
    		V1Deployment deploy = kubeService.queryDeployment(appId, appInfo.getNamespace());
    		if (deploy != null) {
    			V1PodList podList = kubeService.queryPodList(appId, appInfo.getNamespace());
    			for (V1Pod pod : podList.getItems()) {
    				AppInstance inst = new AppInstance();
    				inst.setPodName(pod.getMetadata().getName());
    				inst.setNodeName(pod.getSpec().getNodeName());
    				inst.setHostIP(pod.getStatus().getHostIP());
    				inst.setStatus(pod.getStatus().getPhase());
    				inst.setStartTime(pod.getStatus().getStartTime().toDate());
    				if (pod.getMetadata().getLabels().containsKey("version")) {
    					inst.setAppVersion(pod.getMetadata().getLabels().get("version"));
    				}
    				instlist.add(inst);
    			}
    		}
		} catch (ApiException ex) {
			ex.printStackTrace();
		}
    		
    		Docker docker = appService.selectDockerByNo(appInfo.getDockerNo());
 		
    		PageList<AppVersion> verlist = appService.selectAppVersionList(appInfo.getAppId(), 1, 10);
    		verlist.getList().forEach(ver->{
    			if (ver.getVersion() == appInfo.getAppVersion()) {
    				ver.setCurrent(1);
    			}
    		});
    		
    		model.addObject("docker", docker);
    		model.addObject("instlist", instlist); // 实例
    		model.addObject("verlist", verlist.getList()); // 版本

    		model.addObject("hasDomain", !StringUtils.isEmpty(appInfo.getDomain()));
    		
        model.setViewName("app/appdetail");

        model.addObject("app", appInfo);
        
        return model;
    }
    
	@PostMapping("appinfocreate")
	public JsonResult<Integer> appInfoCreate(
			@RequestBody Map<String, String> params) throws IOException, ApiException {
		
//		AppVersion ver = appService.selectAppVersionLast(ai.getAppId());
//		ai.setAppVersion(ver.getVersion());
//		appService.insertAppInfo(ai);
		
		return JsonResult.ok(0);
	}
    
    @GetMapping(value = "appinfoedit")
    public ModelAndView appInfoEdit(
    		@RequestParam(value = "appid") String appId) {

    		AppInfo  appInfo = appService.selectAppInfoById(appId);
    	
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("app", appInfo);
    		
    		mv.setViewName("/app/appinfoedit");
    		
    		return mv;
    }
    
    @ResponseBody
    @PostMapping(value = "appinfoedit")
    public JsonResult<Integer> appInfoEditAction(
    		@RequestParam(value = "appid") String appId,
    		@RequestParam(value = "environment") String environment,
    		@RequestParam(value = "volume") String volume) throws IOException {

    		AppInfo appInfo = appService.selectAppInfoById(appId);
    	
    		appInfo.setEnvironments(environment);
        appInfo.setVolumes(volume);

    		int n = appService.updateAppInfo(appInfo);
    		return JsonResult.ok(n);
    }
    
    @PostMapping(value = "start")
    @ResponseBody
    public JsonResult<Integer> appStart(
    		@RequestParam(value = "appid") String appId) {
        
	      AppInfo appInfo = appService.selectAppInfoById(appId);
	      if (appInfo == null) {
			return JsonResult.fail(-1, "无效参数。");
		}
  		if (appInfo.getInstanceNum() == 0) {
  			return JsonResult.fail(-1, "请设置实例数量。");
  		}
  		AppVersion ver = appService.selectAppVersionLast(appInfo.getAppId());
  		if (ver == null) {
  			return JsonResult.fail(-1, "请发布应用版本。");
  		}
		instService.start(appInfo, ver);
    	
    		return JsonResult.ok(0);
    }
    
	@PostMapping("app/restart")
	@ResponseBody
	public JsonResult<Integer> appRestart(
			@RequestParam("appid")String appId) {
		
    		return appStart(appId);
    		
	}
    
    @PostMapping(value = "stop")
    @ResponseBody
    public JsonResult<Integer> appStop(
    		@RequestParam(value = "appid") String appId) {

    	AppInfo appInfo = appService.selectAppInfoById(appId);
   		if (appInfo == null) {
   			return JsonResult.fail(-1, "无效参数。");
   		}
   
   		instService.stop(appInfo);
   		
   		return JsonResult.ok(0);
    }
    
    @GetMapping(value = "describe")
    public ModelAndView appDescribe(
    		@RequestParam(value = "appid") String appId) {

    	String describe = "";
		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (appInfo == null) {
				describe = "app not exists.";
			}
			
			V1Deployment deploy = kubeService.queryDeployment(appId, appInfo.getNamespace());
			if (deploy != null)
				describe = deploy.toString();
			else
				describe = "deployment not exists.";
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    		
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("describe", describe);
    		mv.setViewName("/app/appdescribe");
    		
    		return mv;
    }
    
    @GetMapping(value = "servicedescribe")
    public ModelAndView serviceDescribe(
    		@RequestParam(value = "appid") String appId) {

    	String describe = "";
		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (appInfo == null) {
				describe = "app dont exists.";
			}
			
			String svcName = StringUtils.isEmpty(appInfo.getServiceId()) ? appInfo.getAppId() : appInfo.getServiceId();
			V1Service svc = kubeService.queryService(svcName, appInfo.getNamespace());
			if (svc != null)
				describe = svc.toString();
			else
				describe = "service not exists.";
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    		
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("describe", describe);
    		mv.setViewName("/app/servicedescribe");
    		
    		return mv;
    }
    
    @GetMapping(value = "ingressdescribe")
    public ModelAndView ingressDescribe(
    		@RequestParam(value = "appid") String appId) {

    	String describe = "";
		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (appInfo == null) {
				describe = "app dont exists.";
			}
			
			if (StringUtils.isEmpty(appInfo.getDomain())) {
				describe =  "app have no domain.";
			}
			
			ExtensionsV1beta1Ingress ing = kubeService.queryIngress(appId + "-ing", appInfo.getNamespace());
			if (ing != null)
				describe = ing.toString();
			else
				describe = "ingress not exists.";
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    		
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("describe", describe);
    		mv.setViewName("/app/ingressdescribe");
    		
    		return mv;
    }
    
    @GetMapping(value = "eventlist")
    public ModelAndView eventList(
    		@RequestParam(value = "appid") String appId) {

		ModelAndView mv = new ModelAndView();
		
		AppInfo appInfo = appService.selectAppInfoById(appId);
		if (appInfo == null) {
			mv.setViewName("notfound");
			return mv;
		}
		
		List<AppEvent> list = new ArrayList<>();
		try {
			V1beta1EventList eventList = kubeService.queryEventList(appId, appInfo.getNamespace());
			for (V1beta1Event event : eventList.getItems()) {
				AppEvent inst = new AppEvent();
				
				inst.setAction( event.getAction() );
				inst.setEventTime( event.getEventTime().toDate() );
				inst.setKind( event.getKind() );
				inst.setNote( event.getNote() );
				inst.setReason( event.getReason() );
				inst.setType( event.getType() );
				
				list.add(inst);
			}
		} catch (ApiException ex) {
			ex.printStackTrace();
		}
    	
    		mv.addObject("list", list);
    		mv.setViewName("/app/eventlist");
    		
    		return mv;
    }
    
    @GetMapping(value = "changeinstancenum")
    public ModelAndView changeInstanceNum(
    		@RequestParam(value = "appid") String appId) {

    	AppInfo appInfo = appService.selectAppInfoById(appId);
    	
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("app", appInfo);
    		
    		mv.setViewName("/app/changeinstancenum");
    		
    		return mv;
    }
    
    @ResponseBody
    @PostMapping(value = "changeinstancenum")
    public JsonResult<Integer> changeInstanceNumAction(
    		@RequestParam(value = "appid") String appId,
    		@RequestParam(value = "num") int num) throws IOException {

    	try {
			AppInfo appInfo = appService.selectAppInfoById(appId);

    		if (appInfo.getInstanceNum() == num) {
    			return JsonResult.ok(1);
    		} 
    			
			boolean hasDeployment = true;
			try {
				V1Deployment deploy = kubeService.queryDeployment(appInfo.getAppId(), appInfo.getNamespace());
			} catch (ApiException ex) {
				log.info(String.format("query deployment %s, error: %s", appInfo.getAppId(), ex.getLocalizedMessage()));
				if (ex.getCode() == 404) {
					hasDeployment = false;
				}
			}
	
			appInfo.setInstanceNum(num);
			int n = appService.updateAppInfo(appInfo);
			
			if (num == 0) {
				if (hasDeployment) {  // delete deployment.
		    			Deployer deployer = getDeployer(appInfo.getPlatform());
					deployer.delete(appInfo);
					return JsonResult.ok(n);
				}
			}
			
			if (hasDeployment) {  // patch deployment
				List<DeploymentPatchItem> patchlist = new ArrayList<>();
		        patchlist.add(new DeploymentPatchItem("replace", "/spec/replicas", num));
		        
		        //deploy.
		        Gson gson = new Gson();
		        V1Patch patch = new V1Patch(gson.toJson(patchlist));
		        V1Deployment newDeploy = kubeService.patchDeployment(appInfo.getAppId(), appInfo.getNamespace(), patch);
				return JsonResult.ok(n);
			} 
		
			// deploy
			AppVersion ver = appService.selectAppVersionByVersion(appId, appInfo.getAppVersion());
			instService.changeNum(appInfo, ver);
			return JsonResult.ok(n);
		} catch (Exception ex) {
			ex.printStackTrace();
			return JsonResult.fail(-1, ex.getLocalizedMessage());
		}
    }
    
    @GetMapping(value = "changedomain")
    public ModelAndView changeDomain(
    		@RequestParam(value = "appid") String appId) {

    		AppInfo appInfo = appService.selectAppInfoById(appId);
    	
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("app", appInfo);
    		
    		mv.setViewName("/app/changedomain");
    		
    		return mv;
    }
    
    @ResponseBody
    @PostMapping(value = "changedomain")
    public JsonResult<Integer> changeDomainAction(
    		@RequestParam(value = "appid") String appId,
    		@RequestParam(value = "domain2") String domain) throws IOException {

		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (appInfo.getDomain().equalsIgnoreCase(domain)) {
    			return JsonResult.ok(1);
    		} 
			
			appInfo.setDomain(domain);
			int n = appService.updateAppInfo(appInfo);
	
			Deployer deployer = getDeployer(appInfo.getPlatform());
			
			if (StringUtils.isEmpty(appInfo.getDomain())) {
				deployer.deleteIngress(appInfo);
				return JsonResult.ok(n);
			}
			
			deployer.applyIngress(appInfo);
			return JsonResult.ok(n);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return JsonResult.fail(-1, ex.getLocalizedMessage());
		}
    	
    }
    
    @GetMapping(value = "instancelist")
    public ModelAndView instanceList(
			@RequestParam(value = "name", defaultValue = "", required = false) String name,
    			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView model = new ModelAndView();

		PageList<AppInstance> pglist = new PageList<>(pageNo, pageSize);
		try {
			List<V1Pod> podlist = queryPodList(name);
		    pglist.setTotal(podlist.size());
		    
	        Collections.sort(podlist, new Comparator<V1Pod>() {
	
				@Override
				public int compare(V1Pod o1, V1Pod o2) {
					return o2.getStatus().getStartTime().compareTo(o1.getStatus().getStartTime());
				}
	
	        });
	        
	        List<AppInstance> instlist = new ArrayList<>();
	       
	        int offset = (pageNo - 1) * pageSize;
	        int n =0;
	        while (n < pageSize) {
        		if (offset+n >= podlist.size())
        			break;
        		
        		V1Pod pod = podlist.get(offset+n);
        		AppInstance inst = new AppInstance();
				inst.setPodName(pod.getMetadata().getName());
				inst.setNodeName(pod.getSpec().getNodeName());
				inst.setNamespace(pod.getMetadata().getNamespace());
				inst.setHostIP(pod.getStatus().getHostIP());
				inst.setStatus(pod.getStatus().getPhase());
				inst.setStartTime(pod.getStatus().getStartTime().toDate());
				if (pod.getMetadata().getLabels().containsKey("k8s-app")) {
					inst.setAppName(pod.getMetadata().getLabels().get("k8s-app"));
				}
				if (pod.getMetadata().getLabels().containsKey("version")) {
					inst.setAppVersion(pod.getMetadata().getLabels().get("version"));
				}
				instlist.add(inst);
        		++n;
	        }
	        pglist.setList(instlist);
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
        
        PagerInfo pager = new PagerInfo(pageNo, pageSize);
        
        if (pglist.getList() != null) {
	        pager.setTotalCount(pglist.getTotal());
	        
	        model.addObject("instlist", pglist.getList());
        }else {
        	model.addObject("instlist", Collections.emptyList());
        }
        model.addObject("pager", pager);
        model.addObject("name", name);
        
        model.setViewName("app/instancelist");

        return model;
    }
    
    @GetMapping(value = "actionlist")
    public ModelAndView actionList(
    		@RequestParam(value="appid", defaultValue="", required=false)String appId,
    			@RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView model = new ModelAndView();

        PagerInfo pager = new PagerInfo(pageNo, pageSize);
        PageList<AppAction> pglist = appService.selectAppActionList(appId, pageNo, pageSize);
           
        pager.setTotalCount(pglist.getTotal());
        pager.setBaseUrl("/app/actionlist?appid=" + appId);
        
        model.addObject("actionlist", pglist.getList());
        
        model.addObject("pager", pager);
        model.addObject("appId", appId);
        
        model.setViewName("app/actionlist");

        return model;
    }
    
    @GetMapping(value = "instancelog")
    public ModelAndView instanceLog(
    		@RequestParam(value="appid")String appId,
    		@RequestParam(value = "podname") String podName,
    		@RequestParam(value = "tail", required=false, defaultValue="2000")int tailLines) {

    	String log = "";
		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (appInfo == null) {
				log = "app dont exists.";
			}
			
			log = kubeService.getPodLog(podName, appInfo.getNamespace(), tailLines);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    		
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("podname", podName);
    		mv.addObject("tail", tailLines);
    		mv.addObject("log", log);
    		mv.setViewName("/app/instancelog");
    		
    		return mv;
    }
    
	@PostMapping("instancerestart")
	@ResponseBody
	public JsonResult<Integer> instanceRestart(
			@RequestParam("appid")String appId,
			@RequestParam("podid")String podId) {
		
		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (appInfo == null) {
				return JsonResult.fail(-1, "app dont exists.");
			}
			
    		V1Status status = kubeService.deletePod(podId, appInfo.getNamespace(), null);
    			
			return JsonResult.ok(status.getCode());
				
		} catch (Exception ex) {
			ex.printStackTrace();
			return JsonResult.fail(-1, ex.getLocalizedMessage());
		}
	}
    
    @GetMapping(value = "instancedescribe")
    public ModelAndView instanceDescribe(
    		@RequestParam(value="appid")String appId,
    		@RequestParam(value = "podname") String podName) {

    	String describe = "";
		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (appInfo == null) {
				describe = "app dont exists.";
			}
			
			V1Pod v1Pod = kubeService.queryPod(podName, appInfo.getNamespace());
			if (v1Pod != null) {
				describe = v1Pod.toString();
			} else {
				describe = "pod not exists.";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    		
    		ModelAndView mv = new ModelAndView();
    		mv.addObject("podname", podName);
    		mv.addObject("describe", describe);
    		mv.setViewName("/app/instancedescribe");
    		
    		return mv;
    }
    
    @ResponseBody
    @PostMapping(value = "versiondeploy")
    public JsonResult<Integer> versionDeploy(
    		@RequestParam("appid")String appId,
    		@RequestParam("version")String version) throws IOException {

		try {
			AppInfo appInfo = appService.selectAppInfoById(appId);
			if (version.equals(appInfo.getAppVersion())) {
				return JsonResult.fail(-1, "此版本为当前版本，请选择其他版本进行部署。");
			}
	
			AppVersion ver = appService.selectAppVersionByVersion(appInfo.getAppId(), version);
			if (ver == null) {
				return JsonResult.fail(-1, "无效的应用版本。");
			}
			
			instService.deploy(appInfo, ver);
		
			return JsonResult.ok(0);	
		} catch (Exception ex) {
			ex.printStackTrace();
			return JsonResult.fail(-1, ex.getLocalizedMessage());
		}
        
    }
  
	private Deployer getDeployer(String platform) {
		Deployer deployer = deployerFactory.createDeployer(platform);
		appContext.getAutowireCapableBeanFactory().autowireBean(deployer);
		return deployer;
	}
	
	private List<V1Pod> queryPodList(String name) throws ApiException {
		List<V1Pod> list = new ArrayList<>();
	
		String[] excludes = config.getExcludeNamespaces().split(",");
		V1NamespaceList nslist = kubeService.queryNamespaceList();
		for (V1Namespace ns : nslist.getItems()) {
			
			boolean ignore = false;
			for (String prefix : excludes) {
				if (ns.getMetadata().getName().startsWith(prefix)) {
					ignore = true;
					break;
				}
			}
			
			if (!ignore) {
				V1PodList plist = kubeService.queryPodList(null, ns.getMetadata().getName());
				if (name == null || name == "") {
					list.addAll(plist.getItems());
				} else {
					plist.getItems().forEach((pod)-> {
						if (pod.getMetadata().getName().contains(name)) {
							list.add(pod);
						}
					});
				}
			}
		}
		
		return list;
	}
}
