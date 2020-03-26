<div class="layui-logo" style="margin-left:30px;">后台管理</div>
  <ul class="layui-nav layui-layout-right">
      <li class="layui-nav-item">
          <a href="javascript:;">
               ${(auth.adminName)!}
          </a>
          <dl class="layui-nav-child">
              <dd><a href="#">修改密码</a></dd>
          </dl>
      </li>
      <li class="layui-nav-item">
          <a href="/logout">退出</a>
      </li>
  </ul>

  <script type="text/javascript">
      function changepwd() {
          //iframe层
          layui.use('layer', function () {
              var layer = layui.layer;
              layer.open({
                  type: 2,
                  title: '修改密码',
                  shadeClose: false,
                  shade: 0.8,
                  area: ['400px', '300px'],
                  content: '/changePwd'
              });
          });
      }
  </script>