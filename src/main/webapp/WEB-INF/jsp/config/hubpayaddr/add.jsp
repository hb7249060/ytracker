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
	<form action="<c:url value='/admin/business/hubpayaddr/create'/>" method="post" class="form form-horizontal" id="form-member-add">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>三方名称：</label>
			<div class="formControls col-xs-8 col-sm-9">
<%--				<input type="text" class="input-text" placeholder="三方名称" id="hubName" name="hubName" list="hubNameList">--%>
<%--				<datalist id="hubNameList"></datalist>--%>
				<select class="multiselect" name="targetHubIds" id="targetHubIds" multiple="multiple" style="width:70%;height: 260px;">
				</select>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>收银台地址：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="" placeholder="" id="payAddr" name="payAddr">
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
<script type="text/javascript" src="<c:url value="/static/lib/jquery-ui-1.13.2/jquery-ui.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.uix.multiselect/js/jquery.uix.multiselect.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/jquery.uix.multiselect/js/locales/jquery.uix.multiselect_zh-CN.js" />"></script>
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
		getAllHubs();
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
		data: 'state=1',
		success: function (data) {
			var html = "";
			// var html = "";
			if(data.data != null) {
				for (var i = 0; i < data.data.length; i++) {
					var obj = data.data[i];
					if (obj.id == defProj) {
						html += "<option value='" + obj.id + "'>" + obj.name + "</option>";
					} else {
						html += "<option value='" + obj.id + "'>" + obj.name + "</option>";
					}
				}
			}
			// $("#hubNameList").html(html);
			$("#targetHubIds").html(html);

			//multiselect
			$('#targetHubIds').multiselect({
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
			$('#targetHubIds').on('multiselectChange', function(evt, ui) {
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