<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>应用管理</title>
  <script type="text/javascript" src="/lib/js/jquery.js"></script>
  <script type="text/javascript" src="/lib/layui/layui.js"></script>
  <link rel="stylesheet" href="/lib/layui/css/layui.css">
  <link rel="stylesheet" href="/lib/css/base.css">
  <style>
  </style>
</head>

<body class="layui-layout-body">
  <div class="layui-layout layui-layout-admin">
    <div class="layui-header">
      <#include '/common/top.ftl'>
    </div>

    <div class="layui-side layui-bg-black">
      <div class="layui-side-scroll">
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<#include '/common/left.ftl' />
      </div>
    </div>

    <div class="layui-body">
        <div class="place">
            您所在的位置: 应用列表 > 应用详情
        </div>
      <!-- 内容主体区域 -->
      <div class="manage" style="padding: 15px;">
	     
	     <div>
	       应用信息
	        <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:editAppInfo()">编辑</a>
           <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:viewConfig()">配置信息</a>
         <#if app.state.constant==1>
	       <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:changeNum()">调整数量</a>
           <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:changeDomain()">调整域名</a>
         </#if>  
	     </div>
	     
	     <table class="layui-table">
	     	<tr>
	     	    <td>名称</td>
	     	    <td>${app.name}</td>
	     	    <td>产品编号</td>
                <td>${app.productNo}</td>
                <td>状态</td>
                <td>${app.state}</td>
	     	</tr>
	     	<tr>	
                <td>实例数</td>
                <td>${app.instanceNum}</td>
	     		<td>CPU</td>
	     		<td>${docker.cpuNum}</td>
	     		<td>内存</td>
	     		<td>${docker.memory?c} MB</td>
	     	</tr>
	     	<tr>
                <td>分组</td>
                <td>${app.groupId?c}</td>
                 <td>平台</td>
                <td>${app.platform}</td>
	     		<td>域名</td>
	     		<td><#if app.domain??><a target="_blank" href="http://${app.domain}">${app.domain}</a></#if></td>
	     	</tr>
	     	<tr>	
	     		<td>日期</td>
	     		<td>${app.beginDate?date} - ${app.endDate?date}</td>
	     		<td></td>
	     		<td></td>
	     		<td></td>
	     		<td></td>
	     	</tr>
	     </table>
	     <div>
	     应用实例
	     <#if app.state.constant==1>
	     <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:start()">启动</a>
	     <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:stop()">停止</a>
         <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showAppDescribe()">部署描述</a>
         <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showServiceDescribe()">服务描述</a>
         <#if hasDomain >
         <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showIngressDescribe()">Ingress描述</a>
         </#if>
         <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showEventList()">事件</a>
         </#if>
	     </div>
	     <table class="layui-table">
	      <thead>
	           <tr>
	           		<th>实例</th>
                    <th>版本</th>
                    <th>节点</th>
                    <th>主机IP</th>
                    <th>创建时间</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>

               <#if instlist?? >
                <#list instlist as inst>
                    <tr>
                        <td class="left">${(inst.podName)!}</td>
                        <td class="left">${(inst.appVersion)!}</td>
                        <td class="right">${(inst.nodeName)!}</td>
                        <td class="right">${(inst.hostIP)!}</td>
                        <td>${inst.startTime?datetime}</td>
						<td>${inst.status}</td>
						<td>
                            <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showDescribe('${inst.podName}')">描述</a>
                      		<a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showLog('${inst.podName}')">日志</a>
						</td>
                    </tr>
                </#list>
              </#if>
 		  </table>
 		  <div>
 		     应用版本
 		  </div>
 		   <table class="layui-table">
	      <thead>
	           <tr>
                    <th>应用版本</th>
                    <th>文件名</th>
                    <th>数据名</th>
                    <th>数据版本</th>
                    <th>添加时间</th>
                    <th>操作</th>
                </tr>
                </thead>

				<#if verlist?? >
                <#list verlist as ver>
                    <tr>
                        <td class="left">${(ver.version)!}</td>
                        <td>${(ver.filename)!}</td>
                        <td>${(ver.dataName)!}</td>
                        <td>${(ver.dbVersion)!}</td>
                        <td>${(ver.addTime)?datetime}</td>
						<td>
						<#if app.state.constant==1 >
							<a href="javascript:deployVersion('${ver.version}')" class="layui-btn  layui-btn-normal layui-btn-sm">部署</a>
						</#if>	
						</td>
                    </tr>
                </#list>
                </#if>
 		  </table>
 		  
      </div>
       
    </div>

    <div class="layui-footer">
      <!-- 底部固定区域 -->
      <#include '/common/bottom.ftl' >
    </div>
  </div>
  
	<script type="text/javascript">
		function changeNum() {
			//iframe层
	        layui.use('layer', function () {
	            var layer = layui.layer;
	            layer.open({
	                type: 2,
	                title: '调整实例数量',
	                shadeClose: false,
	                shade: 0.8,
	                area: ['600px', '400px'],
	                content: '/app/changeinstancenum?appid=${app.appId}'
	            });
	        });
		}
		
		function changeDomain() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '调整域名',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['600px', '400px'],
                    content: '/app/changedomain?appid=${app.appId}'
                });
            });
        }
        
		function editAppInfo() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '修改应用信息',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['650px', '450px'],
                    content: '/app/appinfoedit?appid=${app.appId}'
                });
            });
        }
        
        function viewConfig() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '应用配置信息',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['650px', '450px'],
                    content: '/app/appconfiglist?appid=${app.appId}'
                });
            });
        }
        
        function showAppDescribe() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '部署描述',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['900px', '600px'],
                    content: '/app/describe?appid=${app.appId}'
                });
            });
        }
        
        function showServiceDescribe() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '服务描述',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['900px', '600px'],
                    content: '/app/servicedescribe?appid=${app.appId}'
                });
            });
        }
        
        function showIngressDescribe() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: 'Ingress描述',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['900px', '600px'],
                    content: '/app/ingressdescribe?appid=${app.appId}'
                });
            });
        }
        
        function showEventList() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '事件',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['900px', '600px'],
                    content: '/app/eventlist?appid=${app.appId}'
                });
            });
        }
        
		function start() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.confirm('确定要启动应用吗？', {
                        btn: ['确定', '取消']
                    }, function () {
                        $.post('/app/start?appid=${app.appId}', function(json) {
                            if (json.errCode==0) {
                                location.reload();
                            } else {
                                layer.msg(json.errMsg);
                            }
                        });
                    }
                 );
            });
        }
        
        function stop() {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.confirm('确定要停止应用吗？', {
                        btn: ['确定', '取消']
                    }, function () {
                        $.post('/app/stop?appid=${app.appId}', function(json) {
                            if (json.errCode==0) {
                                location.reload();
                            } else {
                                layer.msg(json.errMsg);
                            }
                        });
                    }
                 );
            });
        }
        
		function showLog(podname) {
			//iframe层
	        layui.use('layer', function () {
	            var layer = layui.layer;
	            layer.open({
	                type: 2,
	                title: '实例日志',
	                shadeClose: false,
	                shade: 0.8,
	                area: ['900px', '600px'],
	                content: '/app/instancelog?appid=${app.appId}&podname=' + podname
	            });
	        });
		}
		
		function showDescribe(podname) {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '实例描述',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['900px', '600px'],
                    content: '/app/instancedescribe?appid=${app.appId}&podname=' + podname
                });
            });
        }
        
		function deployVersion(ver) {
			$.post('/app/versiondeploy?appid=${app.appId}&version=' + ver, 
				[],
				function(json) {
					layui.use('layer', function () {
			            var layer = layui.layer;
						if (json.errCode == 0) {
	                         layer.msg('操作已提交，系统处理中。', function() {location.reload();});
	                    } else {
	                        layer.msg(json.errMsg);
	                    }
                    });
				}, 
				'json');
		}
		
    </script>    
</body>

</html>