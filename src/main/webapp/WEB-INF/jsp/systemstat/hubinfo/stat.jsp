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
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 统计 <span class="c-gray en">&gt;</span> 三方日终<a class="btn btn-success btn-refresh radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<form id="form" action="<c:url value="/admin/config/hubinfo/stat" />" method="post" >
				<div class="text-l">
<%--					<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" name="datemin" class="input-text Wdate" style="width:120px;">--%>
<%--					---%>
					<input type="text" onfocus="WdatePicker('yyyy-MM-dd')" id="statDate" name="statDate" class="input-text Wdate" style="width:160px;" autocomplete="off">
					<input type="text" class="input-text" style="width:150px" placeholder="码商" id="userName" name="userName" list="hubNameList">
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
							<th width="40">派单量</th>
							<th width="40">成交量</th>
							<th width="40">收益</th>
							<th width="40">成功率</th>
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
		{"data": "name","bSortable": false},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = data.statDate;
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<table class=\"table-bordered\">";
				for(var i = 0;i<data.hubPayChannelList.length;i++) {
					var item = data.hubPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": ￥" + parseFloat(item.stat.orderTotalFee).toFixed(2);
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<table class=\"table-bordered\">";
				for(var i = 0;i<data.hubPayChannelList.length;i++) {
					var item = data.hubPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": ￥" + parseFloat(item.stat.orderPayFee).toFixed(2);
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<table class=\"table-bordered\">";
				for(var i = 0;i<data.hubPayChannelList.length;i++) {
					var item = data.hubPayChannelList[i];

					html += "<tr><td>";
					html += "<span>" + item.channelName + ": ￥" + (item.totalProfit != null ? parseFloat(item.totalProfit).toFixed(2) : 0) + "</span>";
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				// var html = (parseFloat(data.daliyStat.orderSuccessRate.replace("%","")) + parseFloat(data.daliyStat.groupOrderSuccessRate.replace("%",""))) / 2 + "%";
				var html = "";
				// html += "<br/>";
				html += "<b>成功率：</b><br/>";
				html += "<table class=\"table-bordered\">";
				for(var i = 0;i<data.hubPayChannelList.length;i++) {
					var item = data.hubPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": " + parseFloat(item.stat.orderSuccessRate).toFixed(2) + "%";
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}}
	];
	var listAction = "<c:url value="/admin/config/hubinfo/stat" />";
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
						html += "<option>" + obj.username + "</option>";
					} else {
						html += "<option>" + obj.username + "</option>";
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