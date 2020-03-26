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
   <form id="form1" action="/app/appinfoedit" method="post" style="padding: 20px">
   	<input type="hidden" name="appid" value="${app.appId}" />
    
    <div class="layui-form-item ">
         <label class="layui-form-label">Environment</label>
        <div class="layui-input-block">
            <textarea name="environment" style="width:450px; height:120px;">${(app.environments)!}</textarea>
        </div>
    </div>
    
	<div class="layui-form-item">
        <label class="layui-form-label">Volume</label>
        <div class="layui-input-block">
            <textarea name="volume" style="width:450px; height:80px;">${(app.volumes)!}</textarea>
        </div>
    </div>
  
	<div class="layui-form-item">
        <label class="layui-form-label">状态</label>
        <div class="layui-input-block">
        		<select name="state" id="state">
                <option value="0">设置中</option>
                <option value="1">正常</option>
                <option value="2">已过期</option>
                <option value="9">已关闭</option>
             </select>
        </div>
    </div>
  
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button id="btn-ok" class="layui-btn layui-btn-normal ">确定</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
 	
	<script type="text/javascript">
	   
        layui.use('form', function() {
           var form = layui.form;
           
           // ...
           form.render();
        });
        
		$(function () {
            $('#state').val(${app.state.constant});
          
	        $('#btn-ok').on('click', function () {
	        
	            $('#form1').ajaxSubmit({
	                dataType: 'json',
	                success: function (json) { 
		                layui.use('layer', function () {
				            var layer = layui.layer;
		                    if (json.errCode == 0) {
	                             layer.msg('操作已成功完成。', function(){ parent.location.reload(); });
		                    } else {
		                        layer.msg(json.errMsg);
		                    }
		                });
	                }
	            });
	
	            return false;
	        });
	    });
	</script>
	
</body>

</html>
