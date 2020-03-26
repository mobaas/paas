<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <title></title>
  <script type="text/javascript" src="/lib/js/jquery.js"></script>
  <script type="text/javascript" src="/lib/js/jquery.form.js"></script>
  <script type="text/javascript" src="/lib/layui/layui.js"></script>
  <link rel="stylesheet" href="/lib/layui/css/layui.css">
  <link rel="stylesheet" href="/lib/css/base.css">
  <style>
  </style>
</head>
<body>
  
    <table class="layui-table" style="padding: 10px">
      <thead>
           <tr>
                <th>Action</th>
                <th>Type</th>
                <th>Kind</th>
                <th>Note</th>
                <th>Reason</th>
                <th>添加时间</th>
            </tr>
            </thead>

            <#if list?? >
            <#list list as ver>
                <tr>
                    <td class="left">${(ver.action)!}</td>
                    <td>${(ver.type)!}</td>
                    <td>${(ver.kind)!}</td>
                    <td>${(ver.note)!}</td>
                    <td>${(ver.reason)!}</td>
                    <td>${(ver.eventTime)?datetime}</td>
                </tr>
            </#list>
            <#else>
            暂无事件。
            </#if>
      </table>
          
 	
	<script type="text/javascript">
		
	</script>
	
</body>

</html>
