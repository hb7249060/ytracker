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
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-2"style="font-weight: bold;color: red;">报警目标群：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<textarea id="ALARM_BOT_TARGET_CHAT_IDS" name="ALARM_BOT_TARGET_CHAT_IDS" cols="" rows="" class="textarea"  placeholder="报警目标群ID，回车换行"></textarea>
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-2"style="font-weight: bold;color: red;">查单过滤词：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<textarea id="BOT_ANALYSIS_FILTER_WORDS" name="BOT_ANALYSIS_FILTER_WORDS" cols="" rows="" class="textarea"  placeholder="查单过滤词，|分隔"></textarea>
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-2"style="font-weight: bold;color: red;">内容替换词：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<textarea id="BOT_REPLACE_FILTER_WORDS" name="BOT_REPLACE_FILTER_WORDS" cols="" rows="" class="textarea"  placeholder="内容替换词，|分隔"></textarea>
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
					$("#ALARM_BOT_TARGET_CHAT_IDS").val(obj.ALARM_BOT_TARGET_CHAT_IDS);
					$("#BOT_ANALYSIS_FILTER_WORDS").val(obj.BOT_ANALYSIS_FILTER_WORDS);
					$("#BOT_REPLACE_FILTER_WORDS").val(obj.BOT_REPLACE_FILTER_WORDS);
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