<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title></title>
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

<!-- 内容主体区域 -->

<form id="form1" class="layui-form" action="/mgnt/adminAdd/action" method="post" style="padding: 0 40px">

    <div class="layui-form-item ">
        <label class="layui-form-label">登录名称</label>
        <div class="layui-input-block">
            <input type="text" name="name" required lay-verify="required" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item ">
        <label class="layui-form-label">密码</label>
        <div class="layui-input-block">
            <input type="password" name="passwd" required lay-verify="required" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item ">
        <label class="layui-form-label">权限</label>
        <div class="layui-input-block">
            <input type="checkbox" name="roles" lay-skin="primary" value="cms" title="内容模块管理">
            <br/>
            <input type="checkbox" name="roles" lay-skin="primary" value="cms-art-pub" title="文章发布">
            <input type="checkbox" name="roles" lay-skin="primary" value="cms-art-audit" title="文章审核">
            <input type="checkbox" name="roles" lay-skin="primary" value="cms-art-review" title="文章审查">
            <br/>
            <input type="checkbox" name="roles" lay-skin="primary" value="company" title="商家模块管理">
            <input type="checkbox" name="roles" lay-skin="primary" value="serviceres" title="服务模块管理">
            <input type="checkbox" name="roles" lay-skin="primary" value="resource" title="资源模块管理">
            <br/>
            <input type="checkbox" name="roles" lay-skin="primary" value="adres" title="广告模块管理">
            <input type="checkbox" name="roles" lay-skin="primary" value="cloudres"
                   title="云模块管理&nbsp;&nbsp;&nbsp;&nbsp;">
            <input type="checkbox" name="roles" lay-skin="primary" value="data" title="数据模块管理">
            <br/>
            <input type="checkbox" name="roles" lay-skin="primary" value="manage" title="系统模块管理">
            <input type="checkbox" name="roles" lay-skin="primary" value="settlement" title="结算模块管理">
            <input type="checkbox" name="roles" lay-skin="primary" value="city" title="城市数据管理">
            <br>
            <input type="checkbox" name="roles" lay-skin="primary" value="resourceNew" title="新资源模块管理">

        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">状态</label>
        <div class="layui-input-block">
            <select name="state" lay-verify="required">
                <option value="0">启用</option>
                <option value="1">禁用</option>
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

    $(function () {
        $('#btn-save').on('click', function () {
            Submit()
            $('#form1').ajaxSubmit({
                dataType: 'json',
                success: function (data) { //
                    if (data.errCode == -1) {
                        layer.alert('用户名已存在！');
                    } else {
                        layer.msg('添加成功。', function () {
                            parent.location.reload();
                        });
                    }
                }
            });

            return false;
        });
    });


    var SelectFalse = false; //用于判断是否被选择条件
    function Submit() {
        var chboxValue = [];
        var CheckBox = $('input[name = roles]');//得到所的复选框

        for (var i = 0; i < CheckBox.length; i++) {

//jquery1.6以上可以if(CheckBox[i].prop('checked') == true)去判断checkbox是否被选中
            if (CheckBox[i].checked)//如果有1个被选中时
            {
                SelectFalse = true;
                chboxValue.push(CheckBox[i].value)//将被选择的值追加到
            }
        }

        if (!SelectFalse) {
            layer.alert('对不起：权限至少要选一项');
            return false
        }
    }
</script>

</body>

</html>


