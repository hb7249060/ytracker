<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--_meta 作为公共模版分离出去-->
<jsp:include page="../_meta.jsp" />
<!--/meta 作为公共模版分离出去-->

</head>
<body>
<!--_header 作为公共模版分离出去-->
<%--<jsp:include page="../_header.jsp" />--%>
<!--/_header 作为公共模版分离出去-->

<!--_menu 作为公共模版分离出去-->
<%--<jsp:include page="../_menu.jsp" />--%>
<!--/_menu 作为公共模版分离出去-->

<section>
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>系统管理 <span class="c-gray en">&gt;</span> 系统配置<a class="btn btn-success btn-refresh radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<form action="<c:url value='/admin/system/settings/save'/>" method="post" class="form form-horizontal" id="form-member-add">
				<div class="row cl" style="display: none;">
					<label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>Web接口加密：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input onclick="$('#WEB_API_ENCRYPT').val(this.checked ? '1' : '0')" class="checkbox-btn" data-checkbox_state="unchecked" data-style_state="off" id="checkbox-item-WEB_API_ENCRYPT_CHK" type="checkbox" value="" />
						<label for="checkbox-item-WEB_API_ENCRYPT_CHK"></label>
						<input type="hidden" id="WEB_API_ENCRYPT" name="WEB_API_ENCRYPT" value="0" />
					</div>
				</div>
				<div class="row cl" style="display: none;">
					<label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>Web默认公钥：</label>
					<div class="formControls col-xs-7 col-sm-8">
						<input type="text" class="input-text" value="" placeholder="WebLogin默认公钥" id="DEFAULT_WEB_API_PUB_KEY" name="DEFAULT_WEB_API_PUB_KEY" />
					</div>
					<div class="formControls col-xs-1 col-sm-2">
						<input class="hui-btn hui-btn-secondary radius" type="button" value="生成" onclick="javascript:generateKey('DEFAULT_WEB_API_PUB_KEY');" />
					</div>
				</div>
				<div class="row cl" style="display: none;">
					<label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>Web默认私钥：</label>
					<div class="formControls col-xs-7 col-sm-8">
						<input type="text" class="input-text" value="" placeholder="WebLogin默认私钥" id="DEFAULT_WEB_API_PRI_KEY" name="DEFAULT_WEB_API_PRI_KEY" />
					</div>
					<div class="formControls col-xs-1 col-sm-2">
						<input class="hui-btn hui-btn-secondary radius" type="button" value="生成" onclick="javascript:generateKey('DEFAULT_WEB_API_PRI_KEY');" />
					</div>
				</div>
				<div class="row cl" <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
					<label class="form-label col-xs-4 col-sm-2" style="color: red;font-weight: bold">系统费率：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="" id="CHARGE_RATE" name="CHARGE_RATE" />
					</div>
				</div>
				<div class="row cl" <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
					<label class="form-label col-xs-4 col-sm-2" style="color: red;font-weight: bold">系统收费方式：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box">
							<select class="select" size="1" name="CHARGE_MODE" id="CHARGE_MODE">
								<option value="0">请选择</option>
								<option value="1">预付费</option>
								<option value="2">后付费</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl" <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
					<label class="form-label col-xs-4 col-sm-2" style="color: red;font-weight: bold">代付功能状态：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box">
							<select class="select" size="1" name="PAY_ORDER_ENABLE" id="PAY_ORDER_ENABLE">
								<option value="">请选择</option>
								<option value="1">可用</option>
								<option value="0">不可用</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl hideSettings">
					<label class="form-label col-xs-4 col-sm-2">连续失败自动停码次数：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="默认为5，填10，则表示连续失败10次自动停码" id="AUTO_DISABLE_CODE_RANGE" name="AUTO_DISABLE_CODE_RANGE" />
					</div>
				</div>
				<div class="row cl hideSettings" style="display: none;">
					<label class="form-label col-xs-4 col-sm-2">系统费率下限：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="如：0.003，表示码商设置费率的最低值" id="MIN_FEE_RATE" name="MIN_FEE_RATE" />
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-2"style="font-weight: bold;color: red;">商户IP白名单：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<textarea id="MERCHANT_WHITE_LIST_IPS" name="MERCHANT_WHITE_LIST_IPS" cols="" rows="" class="textarea"  placeholder="商户下单服务器：请求白名单IP，回车换行"></textarea>
					</div>
				</div>
<%--				<div class="row cl">--%>
<%--					<label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>商户IP黑名单：</label>--%>
<%--					<div class="formControls col-xs-8 col-sm-9">--%>
<%--						<textarea id="MERCHANT_BLOCKED_IPS" name="MERCHANT_BLOCKED_IPS" cols="" rows="" class="textarea"  placeholder="商户下单服务器：请求黑名单IP，回车换行"></textarea>--%>
<%--					</div>--%>
<%--				</div>--%>
<%--				<div class="row cl">--%>
<%--					<label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>码商IP黑名单：</label>--%>
<%--					<div class="formControls col-xs-8 col-sm-9">--%>
<%--						<textarea id="USER_BLOCKED_IPS" name="USER_BLOCKED_IPS" cols="" rows="" class="textarea"  placeholder="码商：请求黑名单IP，回车换行"></textarea>--%>
<%--					</div>--%>
<%--				</div>--%>
				<div class="row cl hideSettings">
					<label class="form-label col-xs-4 col-sm-2">派单保证金：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="输入正整数，如：1000，相当于码商预付款，只对超过预付款的部分派单" id="ORDER_DISPATCHER_DEPOSIT" name="ORDER_DISPATCHER_DEPOSIT" />
					</div>
				</div>
				<div class="row cl hideSettings">
					<label class="form-label col-xs-4 col-sm-2" style="font-weight: bold;color: red;">派单公平模式：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box">
							<select class="select" size="1" name="PO_FAIR_MODE" id="PO_FAIR_MODE">
								<option value="0">关闭</option>
								<option value="1">打开</option>
							</select>
						</span>
						<br/>
						派单公平模式，打开后，将无视码商设置的收款规则。<br/>
						如果需要在派单公平模式时，仍然允许部分码商按规则派单，将到"码商管理"中，将该码商账户类型设置为"特殊"即可。
					</div>
				</div>
				<div class="row cl hideSettings">
					<label class="form-label col-xs-4 col-sm-2">订单金额实付模式：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box">
							<select class="select" size="1" name="ORDER_REAL_PAY_MODE" id="ORDER_REAL_PAY_MODE">
								<option value="0">常规模式</option>
								<option value="1">下浮模式0.01-0.1</option>
								<option value="2">上浮模式0.01-0.1</option>
							</select>
						</span>
						<br/>
						表示在收银台页，展示的收款金额是否和商户下单的金额一致<br/>
						常规表示一致；下浮模式表示实际显示金额比商户下单的少，上浮反之。
					</div>
				</div>
				<div class="row cl hideSettings" <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
					<label class="form-label col-xs-4 col-sm-2" style="font-weight: bold;color: red;">收银台地址：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="输入收银台外网地址" id="PAY_URL" name="PAY_URL" />
					</div>
				</div>
				<div class="row cl" <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
					<label class="form-label col-xs-4 col-sm-2" style="font-weight: bold;color: red;">通知地址：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="输入回调外网地址" id="NOTIFY_URL" name="NOTIFY_URL" />
					</div>
				</div>
				<div class="row cl hostSettings">
					<label class="form-label col-xs-4 col-sm-2" style="font-weight: bold;color: red;">收银台页EIP实例：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="收银台页EIP实例ID" id="PAY_EIP_INSTANCES" name="PAY_EIP_INSTANCES" />
					</div>
				</div>
				<div class="row cl hostSettings">
					<label class="form-label col-xs-4 col-sm-2" style="font-weight: bold;color: red;">收银台页ECS实例：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="收银台页ECS实例ID" id="PAY_ECS_INSTANCES" name="PAY_ECS_INSTANCES" />
					</div>
				</div>
				<div class="row cl hostSettings">
					<label class="form-label col-xs-4 col-sm-2" style="font-weight: bold;color: red;">收银台订阅者：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="收银台订阅者，服务的内网地址" id="PAY_URL_SUBSCRIBES" name="PAY_URL_SUBSCRIBES" />
					</div>
				</div>
				<div class="row cl appSettings">
					<label class="form-label col-xs-4 col-sm-2">APP收费开关：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box">
							<select class="select" size="1" name="APP_CHARGE_STATE" id="APP_CHARGE_STATE">
								<option value="1">收费</option>
								<option value="0">免费</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl appSettings">
					<label class="form-label col-xs-4 col-sm-2">APP收费配置：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<textarea id="APP_CHARGE_CONFIG" name="APP_CHARGE_CONFIG" cols="" rows="" class="textarea"  placeholder="APP收费配置"></textarea>
					</div>
				</div>
				<div class="row cl appSettings">
					<label class="form-label col-xs-4 col-sm-2">APP收益钱包地址：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="填写APP收益钱包地址" id="APP_PROFIT_WALLET_ADDRESS" name="APP_PROFIT_WALLET_ADDRESS" />
					</div>
				</div>
				<div class="row cl appSettings">
					<label class="form-label col-xs-4 col-sm-2">APP是否可用：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box">
							<select class="select" size="1" name="APP_AVALIABLE_STATE" id="APP_AVALIABLE_STATE">
								<option value="0">禁用</option>
								<option value="1">可用</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl appSettings">
					<label class="form-label col-xs-4 col-sm-2">APP下载地址：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="填写APP下载地址" id="APP_DOWNLOAD_URL" name="APP_DOWNLOAD_URL" />
					</div>
				</div>
<%--				<div class="row cl">--%>
<%--					<label class="form-label col-xs-4 col-sm-2">APP审核配置：</label>--%>
<%--					<div class="formControls col-xs-8 col-sm-9">--%>
<%--						<input type="text" class="input-text" value="" placeholder="填写APP审核配置" id="APP_AUDIT_PROP" name="APP_AUDIT_PROP" />--%>
<%--					</div>--%>
<%--				</div>--%>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-2"><span class="c-red">*</span>系统状态：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box">
							<select class="select" size="1" name="SYS_STATE" id="SYS_STATE">
								<option value="1">启用</option>
								<option value="0">禁用</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-2" style="color: red;font-weight: bolder;">* Google验证码：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="" placeholder="请输入当前登录超管账号的Google验证码" name="googleCode">
					</div>
				</div>
				<div class="row cl">
					<div class="col-xs-10 col-sm-9 col-xs-offset-4 col-sm-offset-3">
						<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;保存&nbsp;&nbsp;">
					</div>
				</div>
			</form>
		</article>
	</div>
</section>
<!--_footer 作为公共模版分离出去-->
<jsp:include page="../_footer.jsp" />
<!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="<c:url value="/static/lib/My97DatePicker/4.8/WdatePicker.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.validation/1.14.0/jquery.validate.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.validation/1.14.0/validate-methods.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.validation/1.14.0/messages_zh.js" />"></script>
<script type="text/javascript">
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});

	$(function() {
		$(".hideSettings").css("display", "none");
		$(".hostSettings").css("display", "none");
		$(".appSettings").css("display", "none");
		//修改form表单的action
		$("#form-member-add").attr("action",
				"<c:url value='/admin/system/settings/save'/>");
		//根据id取内容
		var action = '<c:url value='/admin/system/settings/get'/>';
		$.ajax({
			url: action,
			type: 'GET',
			data: '',
			success: function (data) {
				var obj = data.data;
				if (obj != null) {
					if(obj.WEB_API_ENCRYPT == 1) {
						$("#checkbox-item-WEB_API_ENCRYPT_CHK").attr("checked", "true");
						$("#WEB_API_ENCRYPT").val(obj.WEB_API_ENCRYPT);
					};
					$("#DEFAULT_WEB_API_PUB_KEY").val(obj.DEFAULT_WEB_API_PUB_KEY);
					$("#DEFAULT_WEB_API_PRI_KEY").val(obj.DEFAULT_WEB_API_PRI_KEY);
					$("#AUTO_DISABLE_CODE_RANGE").val(obj.AUTO_DISABLE_CODE_RANGE);
					$("#MIN_FEE_RATE").val(obj.MIN_FEE_RATE);
					$("#MERCHANT_WHITE_LIST_IPS").html(obj.MERCHANT_WHITE_LIST_IPS);
					$("#NOTIFY_URL").val(obj.NOTIFY_URL);
					$("#MERCHANT_BLOCKED_IPS").html(obj.MERCHANT_BLOCKED_IPS);
					$("#USER_BLOCKED_IPS").html(obj.USER_BLOCKED_IPS);
					$("#ORDER_DISPATCHER_DEPOSIT").val(obj.ORDER_DISPATCHER_DEPOSIT);
					$("#PO_FAIR_MODE").val(obj.PO_FAIR_MODE);
					$("#ORDER_REAL_PAY_MODE").val(obj.ORDER_REAL_PAY_MODE);
					$("#APP_CHARGE_STATE").val(obj.APP_CHARGE_STATE);
					$("#APP_CHARGE_CONFIG").html(obj.APP_CHARGE_CONFIG);
					$("#APP_PROFIT_WALLET_ADDRESS").val(obj.APP_PROFIT_WALLET_ADDRESS);
					$("#APP_AUDIT_PROP").val(obj.APP_AUDIT_PROP);
					$("#APP_AVALIABLE_STATE").val(obj.APP_AVALIABLE_STATE);
					$("#APP_DOWNLOAD_URL").val(obj.APP_DOWNLOAD_URL);
					$("#PAY_URL").val(obj.PAY_URL);
					$("#PAY_EIP_INSTANCES").val(obj.PAY_EIP_INSTANCES);
					$("#PAY_ECS_INSTANCES").val(obj.PAY_ECS_INSTANCES);
					$("#PAY_URL_SUBSCRIBES").val(obj.PAY_URL_SUBSCRIBES);
					$("#SYS_STATE").val(obj.SYS_STATE);
					$("#CHARGE_MODE").val(obj.CHARGE_MODE);
					$("#CHARGE_RATE").val(obj.CHARGE_RATE);
					$("#PAY_ORDER_ENABLE").val(obj.PAY_ORDER_ENABLE);
					if(!obj.isHost) {
						$(".hostSettings").css("display", "none");
					}
					$(".appSettings").css("display", "none");
				}
			},
			beforeSend: function (xhr) {
			},
			error: function (xhr, data, excption) {
			},
			complete: function (xhr, data) {
			}
		});

	});
	
	$("#form-member-add").validate({
		rules:{
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			layer.confirm('确认要提交吗？',function(index){
				$(form).ajaxSubmit(function (data) {
					//成功callback
					// var index = parent.layer.getFrameIndex(window.name);
					// parent.$('.btn-refresh').click();
					// parent.layer.close(index);
					if(data.code == 200) {
						layer.msg(data.msg);
						// location.reload();
					} else {
						layer.msg(data.msg);
					}
				});
			});
		}
	});

	// 初始化已知checkbox
	init_checkbox_state();
});

function generateKey(domId) {
	var action = '<c:url value='/admin/system/settings/generatePriKey'/>';
	if(domId == 'DEFAULT_WEB_API_PUB_KEY') {
		action = '<c:url value='/admin/system/settings/generatePubKey'/>';
	}
	layer.confirm('确认要重新生成吗？',function(index){
		$.ajax({
			url: action,
			type: 'GET',
			data: '',
			success: function (data) {
				$("#" + domId).val(data.data);
				layer.close(index);
			},
			beforeSend: function (xhr) {
			},
			error: function (xhr, data, excption) {
			},
			complete: function (xhr, data) {
			}
		});
	});
}

</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>