<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title></title>
    <#include "/icon.ftl" />
  <script type="text/javascript" src="/lib/js/jquery.js"></script>
  <script type="text/javascript" src="/lib/js/jquery.form.js"></script>
  <script type="text/javascript" src="/lib/layui/layui.js"></script>
  <link rel="stylesheet" href="/lib/layui/css/layui.css">
  <link rel="stylesheet" href="/lib/css/base.css">
  <style>
    .layui-nav-tree .layui-nav-child .active,
    .layui-nav-tree .layui-nav-child dd:hover {
      background: #0695ff;
    }
  </style>
</head>

<body class="layui-layout-body">

      <!-- 内容主体区域 -->
      <div class="manage" style="padding: 15px;">
		<form id="form1" class="layui-form" action="/mgnt/changePwd/action"  method="post">
		
			<div class="layui-form-item ">
                <label class="layui-form-label">原密码</label>
                <div class="layui-input-block">
                    <input type="password" name="passwd" required lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>
            
            <div class="layui-form-item ">
                <label class="layui-form-label">新密码</label>
                <div class="layui-input-block">
                    <input type="password" name="passwd2" required lay-verify="required" autocomplete="off" class="layui-input">
                </div>
            </div>

            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" id="btn-save" >保存</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>
			
      </div>
   
  </div>
  
<script type="text/javascript">
   layui.use('form', function () {
         var form = layui.form;
         form.render();
     });
     
	$(function() {
     	$('#btn-save').on('click', function() {
     	
     		$('#form1').ajaxSubmit({
     			dataType: 'json',
     		   success: function(data) { //
	               if (data.errCode == -1) {
	               		layer.alert('原密码不正确！');
	               } else {
	               		layer.msg('密码修改成功。', function(){
	               			parent.layer.closeAll();
						});
	               }
	            }
     		});
     		
     		return false;
     	});
     });
     
</script>

</body>

</html>


