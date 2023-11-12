<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--_meta 作为公共模版分离出去-->
<jsp:include page="../../_meta.jsp" />
<!--/meta 作为公共模版分离出去-->
<link rel="stylesheet" type="text/css" href="<c:url value="/static/lib/jquery-ui-1.13.2/jquery-ui.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/static/lib/jquery.uix.multiselect/css/jquery.uix.multiselect.css" />" />
<style rel="stylesheet" type="text/css">
	.ui-state-highlight {
		height: auto;
		margin-top: 0px;
	}
</style>

</head>
<body>
<article class="cl pd-20">
	<form action="<c:url value='/admin/config/hubpaychannel/create'/>" method="post" class="form form-horizontal" id="form-member-add">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方名称：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" placeholder="三方名称" id="hubName" name="hubName" list="hubNameList">
				<datalist id="hubNameList"></datalist>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>绑定系统通道：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" placeholder="绑定系统通道" id="sysChannelName" name="sysChannelName" list="sysChannelNameList">
				<datalist id="sysChannelNameList"></datalist>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>绑定商户：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<div id="TargetHubMultiSelectDiv">
					<select class="multiselect" name="bindMchIds" id="bindMchIds" multiple="multiple" style="width:70%;height: 260px;">
					</select>
				</div>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>费率：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="费率，示例：万500输入0.05" name="rate" id="rate">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>权重：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="1-100，值越大，权重越大，派单越多" name="weight" id="weight">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>类型：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<span class="select-box">
					<select class="select" size="1" name="type" id="type">
						<option value="ds">代收</option>
						<option value="df">代付</option>
					</select>
				</span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>加密类型：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<span class="select-box">
					<select class="select" size="1" name="encType" id="encType">
						<option value="zy">自营</option>
						<option value="sf">三方1-XXX</option>
					</select>
				</span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方通道名称[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="三方通道名称" name="confName" id="confName">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方通道编码[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="三方通道编码" name="confPayCode" id="confPayCode">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方商户ID[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="三方商户ID" name="confMchId" id="confMchId">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方商户公钥[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="三方商户公钥" name="confMchPubKey" id="confMchPubKey">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方商户私钥[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="三方商户私钥" name="confMchPriKey" id="confMchPriKey">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方下单地址[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="三方下单地址[代收]" name="confMchPoUrl" id="confMchPoUrl">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方查单地址[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="三方查单地址[代收]" name="confMchQoUrl" id="confMchQoUrl">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方回调IP[代收]：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<textarea id="confMchCallbackIp" name="confMchCallbackIp" cols="" rows="" class="textarea"  placeholder="三方回调IP[代收]"></textarea>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>状态：</label>
			<div class="formControls col-xs-6 col-sm-9">
				<span class="select-box">
					<select class="select" size="1" name="usable" id="usable">
						<option value="1">启用</option>
						<option value="0">停用</option>
					</select>
				</span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<textarea id="memo" name="memo" cols="" rows="" class="textarea"  placeholder=""></textarea>
			</div>
		</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3" style="color: red;font-weight: bolder;">* Google验证码：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="请输入当前登录超管账号的Google验证码" name="googleCode">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input type="hidden" id="id" name="id">
				<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>

<!--_footer 作为公共模版分离出去-->
<jsp:include page="../../_footer.jsp" />
<!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="<c:url value="/static/lib/My97DatePicker/4.8/WdatePicker.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.validation/1.14.0/jquery.validate.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.validation/1.14.0/validate-methods.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.validation/1.14.0/messages_zh.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery-ui-1.13.2/jquery-ui.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.uix.multiselect/js/jquery.uix.multiselect.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.uix.multiselect/js/locales/jquery.uix.multiselect_zh-CN.js" />"></script>
<%--<script type="text/javascript" src="<c:url value="/static/lib/jquery.uix.multiselect/js/jquery.multiselect.filter.js" />"></script>--%>
<script type="text/javascript">
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});

	//取页面传来的id
	var id = '${param.id}';
	//alert(id==''); add时，id='' is true
	$(function() {
		//如果id不为''，则为编辑页面，获取并填充表单数据
		if (id != '') {
			//修改form表单的action
			$("#form-member-add").attr("action",
					"<c:url value='/admin/config/hubpaychannel/update'/>");
			$("#id").val(id);
			//根据id取内容
			var action = '<c:url value='/admin/config/hubpaychannel/getOne/'/>' + id;
			$.ajax({
				url: action,
				type: 'GET',
				data: '',
				success: function (data) {
					var obj = data.data;
					$("#hubName").val(obj.hubName);
					$("#sysChannelName").val(obj.channelName);
					getAllMerchants(obj.mchId);
					$("#rate").val(obj.rate);
					$("#weight").val(obj.weight);

					$("#type").val(obj.type);
					$("#encType").val(obj.encType);
					$("#confName").val(obj.confName);
					$("#confPayCode").val(obj.confPayCode);
					$("#confMchId").val(obj.confMchId);
					$("#confMchPubKey").val(obj.confMchPubKey);
					$("#confMchPriKey").val(obj.confMchPriKey);
					$("#confMchPoUrl").val(obj.confMchPoUrl);
					$("#confMchQoUrl").val(obj.confMchQoUrl);
					$("#confMchCallbackIp").val(obj.confMchCallbackIp);

					$("#usable").val(obj.usable);
					$("#memo").html(obj.memo);
					$("#state").val(obj.state);
					getAllPayChannel(obj.channelId);
					getAllHubs(obj.hubId);
				},
				beforeSend: function (xhr) {
				},
				error: function (xhr, data, excption) {
				},
				complete: function (xhr, data) {
				}
			});
		} else {
			getAllMerchants();
			getAllPayChannel();
			getAllHubs();
		}
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
					if(data.code == 200) {
						//成功callback
						var index = parent.layer.getFrameIndex(window.name);
						// parent.$('.btn-refresh').click();
						// parent.layer.close(index);
						parent.location.reload();
					} else {
						// layer.close(layer.index);
						layer.msg(data.msg);
					}

				});
			});
		}
	});

});

//查询所有三方
function getAllHubs(defProj) {
	var action = '<c:url value='/admin/config/hubinfo/getall'/>';
	$.ajax({
		url: action,
		type: 'GET',
		data: '',
		success: function (data) {
			var html = "";
			// var html = "";
			if(data.data != null) {
				for (var i = 0; i < data.data.length; i++) {
					var obj = data.data[i];
					if (obj.id == defProj) {
						html += "<option>" + obj.name + "</option>";
					} else {
						html += "<option>" + obj.name + "</option>";
					}
				}
			}
			$("#hubNameList").html(html);
		},
		beforeSend: function (xhr) {
		},
		error: function (xhr, data, excption) {
		},
		complete: function (xhr, data) {
		}
	});
}

//查询所有系统通道
function getAllPayChannel(defProj) {
	var action = '<c:url value='/admin/system/paychannel/getall'/>';
	$.ajax({
		url: action,
		type: 'GET',
		data: '',
		success: function (data) {
			var html = "";
			// var html = "";
			if(data.data != null) {
				for (var i = 0; i < data.data.length; i++) {
					var obj = data.data[i];
					if (obj.id == defProj) {
						html += "<option>" + obj.name + "</option>";
					} else {
						html += "<option>" + obj.name + "</option>";
					}
				}
			}
			$("#sysChannelNameList").html(html);
		},
		beforeSend: function (xhr) {
		},
		error: function (xhr, data, excption) {
		},
		complete: function (xhr, data) {
		}
	});
}

//查询所有商户
function getAllMerchants(defProj) {
	var action = '<c:url value='/admin/config/mchinfo/getall'/>';
	$.ajax({
		url: action,
		type: 'GET',
		data: '',
		success: function (data) {
			var html = "";
			// var html = "";
			if(data.data != null) {
				for (var i = 0; i < data.data.length; i++) {
					var obj = data.data[i];
					if (defProj != undefined && defProj.indexOf(obj.id) != -1) {
						html += "<option selected='selected' value='" + obj.id + "'>" + obj.name + "</option>";
					} else {
						html += "<option value='" + obj.id + "'>" + obj.name + "</option>";
					}
				}
			}
			$("#bindMchIds").html(html);

			//multiselect
			$('#bindMchIds').multiselect({
				header: true,
				height: 175,
				minWidth: 225,
				classes: '',
				checkAllText: '选中全部',
				uncheckAllText: '取消全选',
				noneSelectedText: '请勾选',
				selectedText: '# 选中',
				selectedList: 5,
				show: null,
				hide: null,
				autoOpen: false,
				multiple: true,
				position: {},
				appendTo: "body",
				menuWidth:null
			});
			$('#bindMchIds').on('multiselectChange', function(evt, ui) {
				var values = $.map(ui.optionElements, function(opt) { return $(opt).attr('value'); }).join(',');
				console.log('Multiselect change event! ' + (ui.optionElements.length == $('#targetUserNames').find('option').size() ? 'all ' : '') + (ui.optionElements.length + ' value' + (ui.optionElements.length > 1 ? 's were' : ' was')) + ' ' + (ui.selected ? 'selected' : 'deselected') + ' (' + values + ')');
				// $("#targetUserNames").val(values);
			})
		},
		beforeSend: function (xhr) {
		},
		error: function (xhr, data, excption) {
		},
		complete: function (xhr, data) {
		}
	});
}

</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>