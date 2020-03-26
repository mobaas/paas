<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>后台管理</title>
<script type="text/javascript" src="/lib/js/jquery.js"></script>
<script type="text/javascript" src="/lib/layui/layui.js"></script>
<link rel="stylesheet" href="/lib/layui/css/layui.css">
<link rel="stylesheet" href="/lib/css/base.css">
<link rel="stylesheet" href="/lib/css/langmu.css">
<style>
.layui-nav-tree .layui-nav-child .active, .layui-nav-tree .layui-nav-child dd:hover
	{
	background: #0695ff;
}
</style>
</head>

<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header"><#include '/common/top.ftl'></div>

		<div class="layui-side layui-bg-black">
			<div class="layui-side-scroll">
				<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
				<@includeX template='/common/left.ftl' />
			</div>
		</div>

		<div class="layui-body">
			<div class="place">您所在的位置: 管理首页</div>
			<!-- 内容主体区域 -->
			<!--  <div class="manage" style="padding: 15px;"> -->
			<div style="margin:30px;">
				没有权限！
			</div>


			<!-- </div> -->
		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			<#include '/common/bottom.ftl' >
		</div>
	</div>


</body>

</html>
<script>
   

</script>
