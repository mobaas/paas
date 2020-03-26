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
   <form id="form1" action="/app/changeinstancenum" method="post" style="padding: 20px 40px">
   	<input type="hidden" name="appid" value="${app.appId}" />
    
    <div class="layui-form-item ">
         <label class="layui-form-label"><span style="color: red;">*</span>名称</label>
        <div class="layui-input-block">
            <input type="text" name="name" readonly="readonly" autocomplete="off" class="layui-input" value="${app.name}">
        </div>
    </div>
    
	<div class="layui-form-item">
        <label class="layui-form-label">现有实例数</label>
        <div class="layui-input-block">
            <input type="text" name="instanceNum" readonly="readonly" autocomplete="off" class="layui-input" value="${app.instanceNum}">
        </div>
    </div>
  
	<div class="layui-form-item">
        <label class="layui-form-label">调整实例数</label>
        <div class="layui-input-block">
        		<input type="text" id="num" name="num" required lay-verify="required"
        			autocomplete="off" class="layui-input" value="">
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
		$(function () {
	        $('#btn-ok').on('click', function () {
	        
	             var num = $("#num").val();
                 if (num == '') {
	                	 alert('请输入实例数量。');
	                	 return false;
                 }
                 
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
