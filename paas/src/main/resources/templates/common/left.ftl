<ul class="layui-nav layui-nav-tree" lay-filter="test">
    
    <li class="layui-nav-item <#if module=="app">layui-nav-itemed</#if>">
        <a href="javascript:;">应用管理</a>
        <dl class="layui-nav-child">
            <dd <#if path?starts_with('/app/applist')>class="layui-this"</#if>>
                <a href="/app/applist">应用列表</a>
            </dd>
            <dd <#if path?starts_with('/app/inst')>class="layui-this"</#if>>
                <a href="/app/instancelist">实例列表</a>
            </dd>
            <dd <#if path?starts_with('/app/action')>class="layui-this"</#if>>
                <a href="/app/actionlist">操作记录</a>
            </dd>
        </dl>
    </li>

    <li class="layui-nav-item <#if module=="infr">layui-nav-itemed</#if>">
        <a href="javascript:;">资源管理</a>
        <dl class="layui-nav-child">
            <dd <#if path?starts_with("/infr/host")>class="layui-this"</#if>>
                <a href="/infr/hostlist">主机列表</a>
            </dd>
        </dl>
    </li>
    
    <li class="layui-nav-item <#if module=="sys">layui-nav-itemed</#if>">
        <a href="javascript:;">系统管理</a>
        <dl class="layui-nav-child">
            <dd <#if path=="/sys/adminlist">class="layui-this"</#if>>
                <a href="/sys/adminlist">账号管理</a>
            </dd>
        </dl>
    </li>

</ul>

 <script>
     //JavaScript代码区域
     layui.use('element', function () {
         var element = layui.element;
     });
 </script>
