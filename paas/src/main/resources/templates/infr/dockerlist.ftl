<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>数据库列表</title>
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
            您所在的位置: 资源管理 > Docker列表
        </div>
      <!-- 内容主体区域 -->
      <div class="manage" style="padding: 15px;">
	      <form class="layui-form listForm" method="GET">
       
             <div class="layui-inline">
             	<button class="layui-btn  layui-btn-normal" onclick="javascript:add()">添加实例</button>
             </div>
             
          </form>
          
	      <table id="demo1" class="layui-table">
	      <thead>
	           <tr>
                    <th>编号</th>
                    <th>CPU核心</th>
                    <th>CPU占用</th>
                    <th>内存</th>
                    <th>磁盘</th>
                    <th>操作</th>
                </tr>
                </thead>

                <#list list as dck>
                    <tr>
                        <td class="left">${(dck.dockerNo)!}</td>
                        <td class="left">${(dck.cpuNum)!}</td>
                        <td class="left">${(dck.cpuPercent)!}</td>
                        <td class="left">${(dck.memory)!}</td>
                        <td class="left">${(dck.sysDisk)!}</td>
						<td>
							<a href="javascript:edit(${inst.id})" class="layui-btn  layui-btn-normal layui-btn-sm">编辑</a>
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
		function add() {
			//iframe层
	        layui.use('layer', function () {
	            var layer = layui.layer;
	            layer.open({
	                type: 2,
	                title: '添加Docker',
	                shadeClose: false,
	                shade: 0.8,
	                area: ['650px', '550px'],
	                content: '/infr/dockeradd'
	            });
	        });
		}
		
		function edit(id) {
			//iframe层
	        layui.use('layer', function () {
	            var layer = layui.layer;
	            layer.open({
	                type: 2,
	                title: '编辑Docker',
	                shadeClose: false,
	                shade: 0.8,
	                area: ['650px', '550px'],
	                content: '/infr/dockeredit?id=' + id//iframe的url
	            });
	        });
        }
        
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
                            location.href = "/infr/dockerlist?pageNo=" + obj.curr;
                    }
                }
            });
        })
    </script>    
</body>

</html>