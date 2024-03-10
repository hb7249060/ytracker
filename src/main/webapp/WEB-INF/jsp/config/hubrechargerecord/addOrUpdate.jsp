<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--_meta 作为公共模版分离出去-->
<jsp:include page="../../_meta.jsp" />
<!--/meta 作为公共模版分离出去-->

</head>
<body>
<article class="cl pd-20">
	<form action="<c:url value='/admin/business/hubrechargerecord/create'/>" method="post" class="form form-horizontal" id="form-member-add">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方名称：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" placeholder="三方名称" id="hubName" name="hubName" list="hubNameList">
				<datalist id="hubNameList"></datalist>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>金额：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="" id="amount" name="amount">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">备注：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<textarea id="memo" name="memo" cols="" rows="" class="textarea"  placeholder=""></textarea>
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
					"<c:url value='/admin/business/hubrechargerecord/update'/>");
			$("#id").val(id);
			//根据id取内容
			var action = '<c:url value='/admin/business/hubrechargerecord/getOne/'/>' + id;
			$.ajax({
				url: action,
				type: 'GET',
				data: '',
				success: function (data) {
					var obj = data.data;
					$("#hubName").val(obj.hubName);
					$("#telChatId").val(obj.telChatId);
					$("#telGroupName").val(obj.telGroupName);
					$("#memo").val(obj.memo);
					$("#state").val(obj.state);
					$("#groupBotInfo").val(obj.groupBotInfo);
					getAllHubs(obj.hubName);
				},
				beforeSend: function (xhr) {
				},
				error: function (xhr, data, excption) {
				},
				complete: function (xhr, data) {
				}
			});
		} else {
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
						layer.msg(data.msg);
					}
				});
			});
		}
	});


});

//查询所有三方
function getAllHubs(defProj) {
	var action = '<c:url value='/admin/business/hubinfo/getall'/>';
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

</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>