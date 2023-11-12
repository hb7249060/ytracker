<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Google双因子绑定</title>
    <script type="text/javascript" src="<c:url value="/static/lib/jquery/1.9.1/jquery.min.js" />"></script>
</head>
<body>
    <img src="${qrCodeImageBase64}" alt="扫码绑定" />
    <br/>
    <input type="submit" value="立即绑定" onclick="javascript:confirmBinding();">

    <script type="text/javascript">
        function confirmBinding() {
            var googleRegex =/\d{6}/;
            var inputGoogleCode = window.prompt("请输入6位google验证码");
            if(googleRegex.test(inputGoogleCode)){
                $.ajax({
                    url:"<c:url value='/admin/bindingGoogleTwoFactorValidate' />",
                    type:"post",
                    data:{
                        "aspm":"${aspm}",
                        "randomSecretKey":"${randomSecretKey}",
                        "inputGoogleCode":inputGoogleCode
                    },
                    dataType:"json",
                    success:function (data) {
                        if(data.state==='success'){
                            window.alert("绑定成功");
                            location.href = "<c:url value="/admin/login.html" />";
                        }else if(data.state==='fail'){
                            window.alert("操作失败："+data.msg);
                        }
                    }
                });
            }else {
                window.alert("请正确输入6位google验证码")
            }

        }
    </script>
</body>
</html>