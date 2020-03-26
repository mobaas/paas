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
            您所在的位置: 应用管理 > 操作记录
        </div>
      <!-- 内容主体区域 -->
      <div class="manage" style="padding: 15px;">
	      <form class="layui-form listForm" method="GET">
       
             <div class="layui-inline">
               <label class="layui-form-label">AppId</label>
               <div class="layui-input-inline">
                 <input type="text" name="appid" value="${appid!}"  class="layui-input">
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
                    <th>动作</th>
                    <th>版本</th>
                    <th>状态</th>
                    <th>时间</th>
                    <th>操作</th>
                </tr>
                </thead>

                <#list actionlist as act>
                    <tr>
                        <td class="left">${(act.appId)!}</td>
                        <td class="left">${(act.action)!}</td>
                        <td>${(act.version)!}</td>
                        <td>${(act.state)!}</td>
                        <td>${(act.addTime?datetime)!}</td>
						<td></td>
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
                            location.href = "/app/actionlist?appid=${appId!}&pageNo=" + obj.curr;
                    }
                }
            });
        })
    </script>    
</body>

</html>