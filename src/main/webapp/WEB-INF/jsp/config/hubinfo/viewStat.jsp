<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--_meta 作为公共模版分离出去-->
<jsp:include page="../../_meta.jsp" />
<!--/meta 作为公共模版分离出去-->

</head>
<body>
<article class="cl pd-20">
	<form action="" method="post" class="form form-horizontal" id="form-member-add">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>名称：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<span id="name"></span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>系统费率：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<span id="rate"></span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>统计时间：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<span id="statTime"></span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>派单量：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<span id="orderTotalFee" style="font-weight: bolder;color: #333"></span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>总跑量：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<span id="orderPayFee" style="font-weight: bolder;color: #00B83F"></span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3">总收益：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<span id="benfit" style="font-weight: bolder;color: #00B83F"></span>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>总成率：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<span id="orderRate" style="font-weight: bolder;color: #333"></span>
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
			$("#id").val(id);
			//根据id取内容
			var action = '<c:url value='/admin/business/hubinfo/viewStat/'/>' + id;
			$.ajax({
				url: action,
				type: 'GET',
				data: '',
				success: function (data) {
					var obj = data.data;
					$("#name").html(obj.name);
					$("#rate").html(obj.rate);
					$("#statTime").html(datetimeFormat(obj.hubData.updated));
					$("#orderTotalFee").html(obj.hubData.orderTotalFee);
					$("#orderPayFee").html(obj.hubData.orderPayFee);
					$("#orderRate").html(obj.hubData.orderSuccessRate);
					var benfit = obj.hubData.orderPayFee * obj.rate;
					$("#benfit").html(benfit);
				},
				beforeSend: function (xhr) {
				},
				error: function (xhr, data, excption) {
				},
				complete: function (xhr, data) {
				}
			});
		} else {
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


</script> 
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>