<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5.js"></script>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="<c:url value="/static/lib/respond.min.js" />"></script>
<![endif]-->
<link href="<c:url value="/static/h-ui/css/H-ui.min.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/static/h-ui.admin/css/H-ui.login.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/static/h-ui.admin/css/style.css" />" rel="stylesheet" type="text/css" />
<link href="<c:url value="/static/lib/Hui-iconfont/1.0.8/iconfont.css" />" rel="stylesheet" type="text/css" />
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script><![endif]-->
<title>后台登录 - </title>
</head>
<body>
<div class="header"></div>
<div class="loginWraper">
	<div id="loginform" class="loginBox">
		<form class="form form-horizontal" action="<c:url value="/admin/login" />" method="post" onsubmit="return Encrypt()">
			<div class="row cl">
				<label class="form-label col-xs-3" style="line-height: 38px;font-size: larger;color: white">账号：</label>
				<div class="formControls col-xs-8">
					<input id="username" name="username" type="text" placeholder="账号" class="input-text size-L">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-3" style="line-height: 38px;font-size: larger;color: white">密码：</label>
				<div class="formControls col-xs-8">
					<input id="password" name="password" type="password" placeholder="密码" class="input-text size-L">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-3" style="line-height: 38px;font-size: larger;color: white">验证码：</label>
				<div class="formControls col-xs-8">
					<input id="googleCode" name="googleCode" type="text" placeholder="Google验证码（首次登录不填）" class="input-text size-L">
				</div>
			</div>
<%--			<div class="row cl">--%>
<%--				<div class="formControls col-xs-8 col-xs-offset-3">--%>
<%--					<input class="input-text size-L" type="text" placeholder="验证码" onblur="if(this.value==''){this.value='验证码:'}" onclick="if(this.value=='验证码:'){this.value='';}" value="验证码:" style="width:150px;">--%>
<%--					<img src="images/VerifyCode.aspx.png">--%>
<%--					<a id="kanbuq" href="javascript:;">看不清，换一张</a>--%>
<%--				</div>--%>
<%--			</div>--%>
<%--			<div class="row cl">--%>
<%--				<div class="formControls col-xs-8 col-xs-offset-3">--%>
<%--					<label for="online">--%>
<%--						<input type="checkbox" name="online" id="online" value="">--%>
<%--						使我保持登录状态</label>--%>
<%--				</div>--%>
<%--			</div>--%>
			<div class="row cl">
				<div class="formControls col-xs-8 col-xs-offset-3">
					<input name="" type="submit" class="btn btn-success radius size-L" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input name="" type="reset" class="btn btn-default radius size-L" value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;">
				</div>
			</div>
		</form>
	</div>
</div>
<div class="footer">Copyright by...</div>
<input type="text" id="exponent">
<input type="text" id="modulus">
<script type="text/javascript" src="<c:url value="/static/lib/jquery/1.9.1/jquery.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/rsa/Barrett.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/rsa/BigInt.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/rsa/RSA.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/h-ui/js/H-ui.js" />"></script>

<script type="text/javascript">
	$(function(){
		<!--
		$.post("<c:url value="/admin/keyPair" />",{},function(data){
			$("#exponent").val(data.exponent)
			$("#modulus").val(data.modulus)
		},"json")
		-->
	})

	function Encrypt(){
		var statu = false;
		var exponent = $("#exponent").val()
		var modulus = $("#modulus").val()
		var username = $("#username").val()
		var password = $("#password").val()
		if (username.length != 256) {
			setMaxDigits(130);
			var publicKey = new RSAKeyPair(exponent, '', modulus);
			var username = encryptedString(publicKey, encodeURIComponent(username));
			$("#username").val(username);     /*加密后重新赋值到input表单*/
		}
		if (password.length != 256) {
			setMaxDigits(130);
			var publicKey = new RSAKeyPair(exponent, '', modulus);
			var password = encryptedString(publicKey, encodeURIComponent(password));
			$("#password").val(password);
		}
		if(username.length>20&&password.length>20){
			statu = true;
		}
		return statu;
	}
</script>
</body>
</html>