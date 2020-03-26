layui.use('form', function () {
    var form = layui.form;
    form.verify({
// required（必填项）
// phone（手机号）
// email（邮箱）
// url（网址）
// number（数字）
// date（日期）
// identity（身份证）
        cellPhoneRequired: [/^1[3|4|5|7|8]\d{9}$/, '手机格式不正确！'],
        telePhoneRequired: [/^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/, '电话格式不正确！'],
        // 可以不填，只要是填了就验证
        telePhone: function (value, item) {
            console.log(value);
            if (value != "") {
                if (!(/^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/.test(value))) { //
                    return "电话格式不正确";
                }
            }
        },
        // 可以不填，只要是填了就验证
        cellPhone: function (value, item) {
            if (value != '') {
                if (!(/^1[3|4|5|7|8]\d{9}$/.test(value))) { //
                    return "手机格式不正确";
                }
            }

        },
        // 可以不填，只要是填了就验证
        mail: function (value, item) {
            if (value != '') {
                if (!(/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/.test(value))) { //
                    return "邮箱格式不对";
                }
            }

        },
        // 字符不能大于200个
        changdu: function (value, item) {
            if (value.length > 500) {
                return "输入的字符过多";
            }
        },

        isTel: function (value, item) {
            var pattern = /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)|\d{3}-\d{8}|\d{4}-\d{7}/;
            if (value != '') {
                if (!pattern.test(value)) {
                    return '请输入正确的联系方式';
                }
            }
        }
    })
})