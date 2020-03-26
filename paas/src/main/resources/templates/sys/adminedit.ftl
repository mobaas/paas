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

<body>


	<form id="form1" class="layui-form" action="/mgnt/adminEdit/action"  method="post" style="padding:0 40px">
			 <input type="hidden" name="adminid" value="${admin.id}" />

			<div class="layui-form-item ">
                <label class="layui-form-label">登录名称</label>
                <div class="layui-input-block">

                    <input type="text" autocomplete="off" class="layui-input" value=" ${admin.loginName}">
                </div>
            </div>

            <div class="layui-form-item ">
                <label class="layui-form-label">密码</label>
                <div class="layui-input-block">
                    <input type="password" name="passwd" autocomplete="off" class="layui-input" value="........">
                </div>
            </div>

			<div class="layui-form-item ">
                <label class="layui-form-label">权限</label>
                <div class="layui-input-block">
                    <input type="checkbox" name="roles" class="layui-input" <#if roles?seq_contains("cms")>checked="checked"</#if> value="cms">内容模块管理
                </div>
            </div>

             <div class="layui-form-item">
                <label class="layui-form-label">状态</label>
                <div class="layui-input-block">
                    <select name="state" lay-verify="required">
						<option value="0" <#if admin.state==0>selected</#if>>启用</option>
						<option value="1" <#if admin.state==1>selected</#if>>禁用</option>
                    </select>
                </div>
            </div>

            <div class="layui-form-item">
                <div align="center">
                    <button class="layui-btn" id="btn-save">保存</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </form>


<script type="text/javascript">
    layui.use('form', function () {
         var form = layui.form;
         form.render();
     });

     $(function() {
     	$('#btn-save').on('click', function() {
            Submit()
     		$('#form1').ajaxSubmit({
     		   success: function(data) { //
	               parent.location.reload();
	            }
     		});

     		return false;
     	});
     });
    var SelectFalse = false; //用于判断是否被选择条件
    function Submit()
    {
        var chboxValue = [];
        var CheckBox = $('input[name = roles]');//得到所的复选框

        for(var i = 0; i < CheckBox.length; i++)
        {

//jquery1.6以上可以if(CheckBox[i].prop('checked') == true)去判断checkbox是否被选中
            if(CheckBox[i].checked)//如果有1个被选中时
            {
                SelectFalse = true;
                chboxValue.push(CheckBox[i].value)//将被选择的值追加到
            }
        }

        if(!SelectFalse)
        {
            layer.alert('对不起：权限至少要选一项');
            return false
        }
    }
</script>

</body>

</html>


