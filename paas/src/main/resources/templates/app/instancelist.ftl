<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>应用列表</title>
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
            您所在的位置: 应用管理 > 实例列表
        </div>
      <!-- 内容主体区域 -->
      <div class="manage" style="padding: 15px;">
	      <form class="layui-form listForm" method="GET">
       
             <div class="layui-inline">
               <label class="layui-form-label">名称</label>
               <div class="layui-input-inline">
                 <input type="text" name="name" value="${name!}"  class="layui-input">
               </div>
             </div>
             <div class="layui-inline">
                     <button class="layui-btn  layui-btn-normal">查询</button>
             </div>
             
          </form>
          
	      <table id="demo1" class="layui-table">
	      <thead>
	           <tr>
                    <th>实例</th>
                    <th>应用名称</th>
                    <th>空间</th>
                    <th>应用版本</th>
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
                        <td class="left">${(inst.appName)!}</td>
                        <th class="left">${(inst.namespace)!}</td>
                        <td class="left">${(inst.appVersion)!}</td>
                        <td class="right">${(inst.nodeName)!}</td>
                        <td class="right">${(inst.hostIP)!}</td>
                        <td>${inst.startTime?datetime}</td>
						<td>${inst.status}</td>
						<td>
						  <a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showDescribe('${inst.appName}', '${inst.podName}')">描述</a>
                      		<a class="layui-btn  layui-btn-normal layui-btn-sm" href="javascript:showLog('${inst.appName}', '${inst.podName}')">日志</a>
						</td>
                    </tr>
                </#list>
              </#if>
        
 		  </table>
 		  <div id="pager"></div>
      </div>
       
    </div>

    <div class="layui-footer">
      <!-- 底部固定区域 -->
      <#include '/common/bottom.ftl' >
    </div>
  </div>
  
  	<script type="text/javascript">
  		function showLog(appid, podname) {
			//iframe层
	        layui.use('layer', function () {
	            var layer = layui.layer;
	            layer.open({
	                type: 2,
	                title: '实例日志',
	                shadeClose: false,
	                shade: 0.8,
	                area: ['900px', '600px'],
	                content: '/app/instancelog?appid=' + appid + '&podname=' + podname
	            });
	        });
		}
		
		function showDescribe(appid, podname) {
            //iframe层
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.open({
                    type: 2,
                    title: '实例描述',
                    shadeClose: false,
                    shade: 0.8,
                    area: ['900px', '600px'],
                    content: '/app/instancedescribe?appid=' + appid + '&podname=' + podname
                });
            });
        }
        
  	</script>
  	
	<script type="text/javascript">
		
        layui.use(['laypage'], function () {
            var laypage = layui.laypage
                
            var count = [[${(pager.totalCount)?c}]];
            var pageNum = [[${pager.pageNo}]];

            var pageSize = [[${pager.pageSize}]];
            //调用分页

            laypage.render({
                elem: 'pager'
                , theme: '#1E9FFF'
                , count: count
                , curr: pageNum
                , limit: pageSize //每页显示的条数
                , layout: ['count', 'prev', 'page', 'next', 'skip']
                , jump: function (obj, first) {
                    //首次不执行
                  //  console.log(obj)
                    if (!first) {
                       // var str = $(".order-date").find("input").val();
                            location.href = "/app/instancelist?name=${name}&pageNo=" + obj.curr;
                    }
                }
            });
        })
    </script>    
</body>

</html>