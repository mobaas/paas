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
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
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
				<#include '/common/left.ftl'>
			</div>
		</div>

		<div class="layui-body">
			<div class="place">您所在的位置: 管理首页</div>
			<!-- 内容主体区域 -->
			
			 <div class="manage" style="padding: 15px;">
      
                <table class="layui-table">
                    <tr>
                        <td>节点</td>
                        <td>${hostTotal}</td>
                        <td>应用</td>
                        <td>${appTotal}</td>
                        <td>POD</td>
                        <td>${podTotal}</td>
                    </tr>
                </table>
                
    			     <table>
                    <tr>
                        <td><div id="cpuChart" style="height: 500px; width:500px;"></div></td>
                        <td><div id="memoryChart" style="height: 500px; width:500px;"></div></td>
                    </tr>
                </table>
            </div>

		</div>

		<div class="layui-footer">
			<!-- 底部固定区域 -->
			<#include '/common/bottom.ftl' >
		</div>
	</div>

    <script type="text/javascript">
var cpuDom = document.getElementById("cpuChart");
var cpuChart = echarts.init(cpuDom);
cpuOption = null;
cpuOption = {
    tooltip: {
        formatter: 'CPU使用: ${cpuUsage}ms'
    },
    series: [
        {
            name: 'CPU',
            type: 'gauge',
            min: 0,
            max: ${cpuTotal},
            detail: {formatter: '${cpuPercent}%'},
            data: [{value: ${cpuUsage}, name: 'CPU(ms)'}]
        }
    ]
};

if (cpuOption && typeof cpuOption === "object") {
    cpuChart.setOption(cpuOption, true);
}

var memDom = document.getElementById("memoryChart");
var memChart = echarts.init(memDom);
memOption = null;
memOption = {
    tooltip: {
        formatter: '内存使用: ${memoryUsage}GB'
    },
    series: [
        {
            name: '内存',
            type: 'gauge',
            min: 0,
            max: ${memoryTotal},
            detail: {formatter: '${memoryPercent}%'},
            data: [{value: ${memoryUsage}, name: '内存(GB)'}]
        }
    ]
};

if (memOption && typeof memOption === "object") {
    memChart.setOption(memOption, true);
}

setInterval(function () {
    $.get('/metrics', function(json) {
        cpuOption.series[0].data[0].value = json.data.cpuUsage;
        cpuOption.series[0].detail.formatter = json.data.cpuPercent+'%';
        cpuChart.setOption(cpuOption, true);
        
        memOption.series[0].data[0].value = json.data.memoryUsage;
        memOption.series[0].detail.formatter = json.data.memoryPercent+'%';
        memChart.setOption(memOption, true);
    });
    
},2000);

    </script>   
    
</body>

</html>
