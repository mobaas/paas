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
            您所在的位置: 应用管理 > 应用列表
        </div>
      <!-- 内容主体区域 -->
      <div class="manage" style="padding: 15px;">
	      <form class="layui-form listForm" method="GET">
       
             <div class="layui-inline">
             	<a class="layui-btn  layui-btn-normal" href="/app/appadd">添加应用</a>
             </div>
             <div class="layui-inline">
               <label class="layui-form-label">AppId</label>
               <div class="layui-input-inline">
                 <input type="text" name="appid" value="${appid!}"  class="layui-input">
               </div>
             </div>
             <div class="layui-inline">
               <label class="layui-form-label">状态</label>
               <div class="layui-input-inline">
                 <select name="state" id="state">
                    <option value="-1">不限</option>
                    <option value="0">设置中</option>
                    <option value="1">正常</option>
                    <option value="2">已过期</option>
                    <option value="9">已关闭</option>
                 </select>
               </div>
             </div>
             <div class="layui-inline">
                     <button class="layui-btn  layui-btn-normal">查询</button>
             </div>
             
          </form>
          
	      <table id="demo1" class="layui-table">
	      <thead>
	           <tr>
                    <th>AppId</th>
                    <th>名称</th>
                    <th>分组</th>
                    <th>实例数</th>
                    <th>域名</th>
                    <th>Docker产品</th>
                    <th>操作</th>
                </tr>
                </thead>

                <#list list as app>
                    <tr>
                        <td class="left">${(app.appId)!}</td>
                        <td class="left">${(app.name)!}</td>
                        <td>${(app.groupId)?c}</td>
                        <td>${app.instanceNum}</td>
                        <td>${(app.domain)!}</td>
                        <td>${(app.dockerNo)!}</td>
						<td>
							<a href="/app/appdetail?appid=${app.appId}" class="layui-btn  layui-btn-normal layui-btn-sm">详情</a>
						</td>
                    </tr>
                </#list>
        
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
		$(function () {
		  $('#state').val('${state}');
		});
		
		layui.use('form', function() {
		   var form = layui.form;
		   
		   // ...
		   form.render();
		});
		
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
                            location.href = "/app/applist?state=${state}&pageNo=" + obj.curr;
                    }
                }
            });
        })
    </script>    
</body>

</html>