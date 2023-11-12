<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--_meta 作为公共模版分离出去-->
<jsp:include page="../../_meta.jsp" />
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
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 业务管理 <span class="c-gray en">&gt;</span> 业绩统计<a class="btn btn-success btn-refresh radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<form id="form" action="<c:url value="/admin/business/stat/payorderStatData" />" method="post" >
				<div class="text-l">
					<button type="button" onclick="stat(datetimeFormat(+new Date(), 'yyyy-MM-dd'), datetimeFormat(+new Date(), 'yyyy-MM-dd'));" class="btn">今天</button>
					<button type="button" onclick="stat(datetimeFormat(+new Date() - 24*60*60*1000, 'yyyy-MM-dd'), datetimeFormat(+new Date() - 24*60*60*1000, 'yyyy-MM-dd'));" class="btn">昨天</button>
					<button type="button" onclick="stat(datetimeFormat(+new Date() - 2*24*60*60*1000, 'yyyy-MM-dd'), datetimeFormat(+new Date(), 'yyyy-MM-dd'));" class="btn">最近三天</button>
					<button type="button" onclick="stat(datetimeFormat(+new Date() - 6*24*60*60*1000, 'yyyy-MM-dd'), datetimeFormat(+new Date(), 'yyyy-MM-dd'));" class="btn">最近七天</button>
					<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" name="datemin" class="input-text Wdate" style="width:120px;" placeholder="开始日期" autocomplete="off">
					-
					<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" name="datemax" class="input-text Wdate" style="width:120px;" placeholder="结束日期" autocomplete="off">
					<input type="text" class="input-text" style="width:150px" placeholder="码商" id="userName" name="userName" list="userNameList">
					<datalist id="userNameList"></datalist>
					<button type="button" onclick="stat();" class="btn btn-success radius">
						<i class="Hui-iconfont">&#xe665;</i> 查询
					</button>
<%--					<button type="button" onclick="exportExcel();" class="btn btn-success radius"><i class="Hui-iconfont">&#xe644;</i> 按条件导出客户信息</button>--%>
				</div>
			</form>
<%--			<div class="cl pd-5 bg-1 bk-gray mt-20">--%>
<%--				<span class="l">--%>
<%--					<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>--%>
<%--					<a href="javascript:;" onclick="member_add('创建付款码','<c:url value="/admin/business/paycode/addOrUpdate.html" />','1000','')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 创建付款码</a></span>--%>
<%--				</div>--%>
			<div class="mt-20" id="statInfo">
<%--				<table class="table table-border table-bordered table-hover table-bg table-sort">--%>
<%--					<tr>--%>
<%--						<td width="200px">查询条件</td>--%>
<%--						<td class="danger">50000</td>--%>
<%--					</tr>--%>
<%--					<tr>--%>
<%--						<td>码商充值积分</td>--%>
<%--						<td>50000</td>--%>
<%--					</tr>--%>
<%--					<tr>--%>
<%--						<td>订单数量</td>--%>
<%--						<td>50000</td>--%>
<%--					</tr>--%>
<%--					<tr>--%>
<%--						<td>订单总金额</td>--%>
<%--						<td>￥50000</td>--%>
<%--					</tr>--%>
<%--					<tr>--%>
<%--						<td>订单成交金额</td>--%>
<%--						<td class="success">￥50000</td>--%>
<%--					</tr>--%>
<%--					<tr>--%>
<%--						<td>成功率</td>--%>
<%--						<td class="warning">50000</td>--%>
<%--					</tr>--%>
<%--				</table>--%>
<%--				<p><hr/></p>--%>
			</div>
		</article>
	</div>
</section>

<!--_footer 作为公共模版分离出去-->
<jsp:include page="../../_footer.jsp" />
<!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="<c:url value="/static/lib/My97DatePicker/4.8/WdatePicker.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/common/DataTableUtils.js" />"></script>
<script type="text/javascript" src="<c:url value="/static/lib/laypage/1.2/laypage.js" />"></script>
<script type="text/javascript">
$(function(){
	getAllUsers();
	//默认查询今天
	var today = datetimeFormat(+new Date(), "yyyy-MM-dd");
	stat(today, today);
});

//查询统计
function stat(inputStartDate, inputEndDate) {
	var startDate = $("#datemin").val();
	var endDate = $("#datemax").val();
	if(inputStartDate != null) {
		startDate = inputStartDate;
	}
	if(inputEndDate != null) {
		endDate = inputEndDate;
	}
	if(startDate == '' || endDate == '') {
		layer.msg("请输入开始、结束日期");
		return;
	}
	var action = '<c:url value='/admin/business/stat/payorderStatData'/>';
	$.ajax({
		url: action,
		type: 'post',
		data: 'startDate=' + startDate + "&endDate=" + endDate + "&userName=" + $("#userName").val(),
		success: function (data) {
			data = data.data;
			var html = "";
			html += "<table class=\"table table-border table-bordered table-hover table-bg table-sort\">";
			html += "<tr>";
			html += "<td class=\"col-3\">查询条件</td>";

			var dateObj = new Date(); //表示当前系统时间的Date对象
			var year = dateObj.getFullYear(); //当前系统时间的完整年份值
			var month = dateObj.getMonth()+1; //当前系统时间的月份值
			var date = dateObj.getDate(); //当前系统时间的月份中的日
			var day = dateObj.getDay(); //当前系统时间中的星期值
			var weeks = ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
			var week = weeks[day]; //根据星期值，从数组中获取对应的星期字符串
			var hour = dateObj.getHours(); //当前系统时间的小时值
			var minute = dateObj.getMinutes(); //当前系统时间的分钟值
			var second = dateObj.getSeconds(); //当前系统时间的秒钟值
			//如果月、日、小时、分、秒的值小于10，在前面补0
			if(month<10){
				month = "0"+month;
			}
			if(date<10){
				date = "0"+date;
			}
			if(hour<10){
				hour = "0"+hour;
			}
			if(minute<10){
				minute = "0"+minute;
			}
			if(second<10){
				second = "0"+second;
			}
			var nowDate = year+"年"+month+"月"+date+"日 "+hour+":"+minute+":"+second;
			html += "<td class=\"col-9 danger\"><b>" + data.statCondition + "</b><br/>查询时间：" + nowDate + "</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td>订单数量</td>";
			html += "<td>" + data.orderTotalPayCount + "成功 / " + data.orderTotalCount + "总数</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td>订单总金额</td>";
			html += "<td>￥" + data.orderTotalFee + "</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td>订单成交金额</td>";
			html += "<td class=\"success\" style='font-weight: bolder;color: red;'>￥" + data.orderPayFee + "</td>";
			html += "</tr>";
			html += "<tr>";
			html += "<td>成功率</td>";
			html += "<td class=\"warning\" style='font-weight: bolder;color: #333333;'>" + data.orderSuccessRate + "</td>";
			html += "</tr>"
			html += "</table>";
			html += "<p><hr/></p>";
			$("#statInfo").html(html + $("#statInfo").html());
		},
		beforeSend: function (xhr) {
		},
		error: function (xhr, data, excption) {
		},
		complete: function (xhr, data) {
		}
	});
}

//查询所有码商
function getAllUsers(defProj) {
	var action = '<c:url value='/admin/business/userinfo/getall'/>';
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
			$("#userNameList").html(html);
		},
		beforeSend: function (xhr) {
		},
		error: function (xhr, data, excption) {
		},
		complete: function (xhr, data) {
		}
	});
}

/**
 * 搜索按钮的方法
 */
function search(){
	jsonCondition=$("#form").serializeObject();
	console.log(jsonCondition);
}


</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>