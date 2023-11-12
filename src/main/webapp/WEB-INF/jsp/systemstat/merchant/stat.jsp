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
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 系统管理 <span class="c-gray en">&gt;</span> 商户日终<a class="btn btn-success btn-refresh radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<form id="form" action="<c:url value="/admin/config/mchinfo/stat" />" method="post" >
				<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" name="datemin" class="input-text Wdate" style="width:180px;" placeholder="开始日期，默认今天" autocomplete="off">
				-
				<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" name="datemax" class="input-text Wdate" style="width:180px;" placeholder="结束日期，默认今天" autocomplete="off">
<%--				<input type="text" onfocus="WdatePicker('yyyy-MM-dd')" id="statDate" name="statDate" class="input-text Wdate" style="width:160px;">--%>
<%--				<input type="text" class="input-text" style="width:150px" placeholder="输入商户名" id="ccname" name="userName">--%>
				<button type="button" onclick="search();" class="btn btn-success radius">
					<i class="Hui-iconfont">&#xe665;</i> 查询
				</button>
<%--				<button type="button" onclick="exportExcel();" class="btn btn-success radius"><i class="Hui-iconfont">&#xe644;</i> 按条件导出客户信息</button>--%>
<%--			</div>--%>
			</form>
			<div class="mt-20">
				<table class="table table-border table-bordered table-hover table-bg table-sort">
					<thead>
						<tr class="text-c">
<%--							<th width="20"><input type="checkbox" name="" value=""></th>--%>
<%--							<th width="20">ID</th>--%>
<%--							<th width="40">ID</th>--%>
							<th width="20">名称</th>
							<th width="30">统计日期</th>
							<th width="20">生成订单数</th>
							<th width="20">支付成功订单数</th>
							<th width="60">生成订单总金额</th>
							<th width="40">成功支付订单总金额</th>
							<th width="40">订单支付成功率</th>
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
				var html = data != null ? data.statDate : "";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				// var html = data.stat != null ? "<b>总计：" + data.dailyStat.orderTotalCount + "</b>" : "";
				// if(data.dailyStat.payChannelStat != null && data.dailyStat.payChannelStat != undefined) {
				// }
				var html = "<table class=\"table-bordered\">";
				for (var i = 0; i < data.mchPayChannelList.length; i++) {
					var item = data.mchPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": <b>" + item.stat.orderTotalCount + "</b>";
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<table class=\"table-bordered\">";
				for (var i = 0; i < data.mchPayChannelList.length; i++) {
					var item = data.mchPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": <b>" + item.stat.orderTotalPayCount + "</b>";
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<table class=\"table-bordered\">";
				for (var i = 0; i < data.mchPayChannelList.length; i++) {
					var item = data.mchPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": <b>￥" + parseFloat(item.stat.orderTotalFee).toFixed(2) + "</b>";
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<table class=\"table-bordered\">";
				for (var i = 0; i < data.mchPayChannelList.length; i++) {
					var item = data.mchPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": <b>￥" + parseFloat(item.stat.orderPayFee).toFixed(2) + "</b>";
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "<table class=\"table-bordered\">";
				for (var i = 0; i < data.mchPayChannelList.length; i++) {
					var item = data.mchPayChannelList[i];

					html += "<tr><td>";
					html += item.channelName + ": <b>" + item.stat.orderSuccessRate + "</b>";
					html += "</td></tr>";
				}
				html += "</table>";
				return html;
			}}
	];
	var listAction = "<c:url value="/admin/config/mchinfo/stat" />";
	$(dataTableInit(listAction, colModel,'',10));
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

</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>