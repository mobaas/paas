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
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
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
            您所在的位置: 应用管理 > 实例监控
        </div>
      <!-- 内容主体区域 -->
      <div class="manage" style="padding: 15px;">
        <div>
            <a href="hostmetrics?id=${id}">1分钟</a> | 
            <a href="hostmetrics?id=${id}&kind=5m">5分钟</a> | 
            <a href="hostmetrics?id=${id}&kind=15m">15分钟</a> | 
            <a href="hostmetrics?id=${id}&kind=1h">1小时</a> | 
        </div>
	     <div id="container" style="height: 300px"></div>
         <p></p>
         <p></p>
         <div id="container2" style="height: 300px"></div>
         <p></p>
         
      </div>
       
    </div>

    <div class="layui-footer">
      <!-- 底部固定区域 -->
      <#include '/common/bottom.ftl' >
    </div>
  </div>
  
  	<script type="text/javascript">
var dom = document.getElementById("container");
var myChart = echarts.init(dom);
var app = {};
option = null;
option = {
    title: {
        text: 'CPU'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['CPU']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: [${dateList}]
    },
    yAxis: {
        type: 'value',
        max: ${cpuMax}
    },
    series: [
        {
            name: 'CPU',
            type: 'line',
            stack: 'CPU',
            data: [${cpuList}]
        }
    ]
};
;
if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
    </script>    
    
    <script type="text/javascript">
var dom2 = document.getElementById("container2");
var myChart2 = echarts.init(dom2);
var app2 = {};
option2 = null;
option2 = {
    title: {
        text: 'Memory'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['Memory']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: [${dateList}]
    },
    yAxis: {
        type: 'value',
        max: ${memoryMax}
    },
    series: [
        {
            name: 'Memory',
            type: 'line',
            areaStyle: {},
            data: [${memoryList}]
        }
    ]
};
;
if (option2 && typeof option2 === "object") {
    myChart2.setOption(option2, true);
}
    </script>   
    
</body>

</html>