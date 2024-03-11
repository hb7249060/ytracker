<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--_meta 作为公共模版分离出去-->
<jsp:include page="../../_meta.jsp" />
<!--/meta 作为公共模版分离出去-->

</head>
<body>
<!--_header 作为公共模版分离出去-->
<%--<jsp:include page="../../_header.jsp" />--%>
<!--/_header 作为公共模版分离出去-->

<!--_menu 作为公共模版分离出去-->
<%--<jsp:include page="../../_menu.jsp" />--%>
<!--/_menu 作为公共模版分离出去-->

<section>
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 统计 <span class="c-gray en">&gt;</span> 三方数据统计<a class="btn btn-success btn-refresh radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<form id="form" action="<c:url value="/admin/business/stat/getList" />" method="post" >
				<div class="text-l">
<%--					<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" name="datemin" class="input-text Wdate" style="width:120px;">--%>
<%--					---%>
					<input type="text" onfocus="WdatePicker('yyyy-MM-dd')" id="statDate" name="statDate" class="input-text Wdate" style="width:160px;" autocomplete="off">
					<input type="text" class="input-text" style="width:150px" placeholder="三方" id="userName" name="userName" list="hubNameList">
					<datalist id="hubNameList"></datalist>
					<button type="button" onclick="search();" class="btn btn-success radius">
						<i class="Hui-iconfont">&#xe665;</i> 查询
					</button>
<%--					<button type="button" onclick="exportExcel();" class="btn btn-success radius"><i class="Hui-iconfont">&#xe644;</i> 按条件导出客户信息</button>--%>
				</div>
			</form>
			<div class="mt-20">
				<table class="table table-border table-bordered table-hover table-bg table-sort">
					<thead>
						<tr class="text-c">
<%--							<th width="20"><input type="checkbox" name="" value=""></th>--%>
							<th width="20">名称</th>
							<th width="15">日期</th>
							<c:if test="${AdminUser.created == null}">
							<th width="40">总量</th>
							<th width="40">成交量</th>
							<th width="20">收益</th>
							</c:if>
							<th width="20">成功率</th>
							<th width="20">系统余额</th>
							<th width="35">更新时间</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</article>
	</div>
</section>

<!--_footer 作为公共模版分离出去-->
<jsp:include page="../../_footer.jsp" />
<!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="<c:url value="/static/lib/My97DatePicker/4.8/WdatePicker.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/datatables/1.10.15/jquery.dataTables.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/common/DataTableUtils.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/laypage/1.2/laypage.js" />"></script>
<script type="text/javascript">
$(function(){
	//列值定义,clientIP和count都是对应的返回对象的字段
	var colModel = [
		// {"data": "id","bSortable": false},
		{"data": "hubName","bSortable": false},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = data.statDate;
				return html;
			}},
		<c:if test="${AdminUser.created == null}">
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "总" + data.orderTotalCount + "单<br/>￥" + data.orderTotalFee;
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<b style='color: #00B83F'>成功" + data.orderTotalPayCount + "单<br/>￥" + data.orderPayFee + "<b>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<b style='color: #00B83F'>" + data.benfit + "</b>";
				return html;
			}},
		</c:if>
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = data.orderSuccessRate;
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = data.sysBalance != null ? "<b>" + data.sysBalance + "</b>" : "";
				if(data.sysBalance != null && data.sysBalance < 0) {
					html = "<a style='color: red;'>" + html + "</a>";
				}
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = datetimeFormat(data.updated);
				return html;
			}},
	];
	var listAction = "<c:url value="/admin/business/stat/getList" />";
	$(dataTableInit(listAction, colModel,'',10));

	getAllUsers();
});

/**
 * 搜索按钮的方法
 */
function search(){
	jsonCondition=$("#form").serializeObject();
	console.log(jsonCondition);
	var table = $('.table-sort').DataTable();
	table.ajax.reload();
}

/**
 * 改变pageLength自动定位到第一页
 */
$(function(){
	$('.dataTables_length select').on('change',function(){
		$('.table-sort').DataTable().page(0).draw( false );
	});
});

/*用户-添加*/
function member_add(title,url,w,h){
	layer_show(title,url,w,h);
}
/*用户-查看*/
function member_show(title,url,id,w,h){
	layer_show(title,url,w,h);
}
/*用户-停用*/
function member_stop(obj,id){
	layer.confirm('确认要停用吗？',function(index){
		$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_start(this,id)" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-defaunt radius">已停用</span>');
		$(obj).remove();
		layer.msg('已停用!',{icon: 5,time:1000});
	});
}

/*用户-启用*/
function member_start(obj,id){
	layer.confirm('确认要启用吗？',function(index){
		$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_stop(this,id)" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已启用</span>');
		$(obj).remove();
		layer.msg('已启用!',{icon: 6,time:1000});
	});
}
/*用户-编辑*/
function member_edit(title,url,id,w,h){
	layer_show(title,url,w,h);
}
/*密码-修改*/
function change_password(title,url,id,w,h){
	layer_show(title,url,w,h);	
}
//查询所有码商
function getAllUsers(defProj) {
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