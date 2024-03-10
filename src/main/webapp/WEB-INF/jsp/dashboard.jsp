<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--_meta 作为公共模版分离出去-->
<jsp:include page="_meta.jsp" />
<!--/meta 作为公共模版分离出去-->

<meta name="keywords" content="">
<meta name="description" content="">
</head>
<body>
<!--_header 作为公共模版分离出去-->
<%--<jsp:include page="_header.jsp" />--%>
<!--/_header 作为公共模版分离出去-->

<!--_menu 作为公共模版分离出去-->
<%--<jsp:include page="_menu.jsp" />--%>
<!--/_menu 作为公共模版分离出去-->

<section>
	<nav class="breadcrumb"><i class="Hui-iconfont"></i> <a href="/" class="maincolor">首页</a>
		<span class="c-999 en">&gt;</span>
		<span class="c-666">控制台</span>
		<a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<p class="f-20 text-success">
				欢迎来到 <b>聚石塔</b>，您的数据归集专家！
				<p>
				<span class="f-14" id="currTime"></span>
			</p>
			<p><hr></p>

			<table <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if> class="table table-border table-bordered table-hover table-bg table-sort">
				<tr>
					<td class="col-3">三方数量（全部/在用）</td>
					<td class="col-9">
						<b>
							${hubCount} / ${hubCountAvali}
						</b>
					</td>
				</tr>
				<tr>
					<td class="col-3">今日跑量</td>
					<td class="col-9"><b style="color: red;">${totalPayAmount}</b></td>
				</tr>
				<tr>
					<td class="col-3">今日收益</td>
					<td class="col-9"><b style="color: #00B83F;">${totalBenfit}</b></td>
				</tr>
			</table>
			<p><hr></p>
			<table <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if> class="table table-border table-bordered table-hover table-bg table-sort">
				<tr>
					<td class="col-3">昨日跑量</td>
					<td class="col-9"><b style="color: red;">${totalPayAmount2}</b></td>
				</tr>
				<tr>
					<td class="col-3">昨日收益</td>
					<td class="col-9"><b style="color: #00B83F;">${totalBenfit2}</b></td>
				</tr>
			</table>
</article>
</div>
</section>

<!--_footer 作为公共模版分离出去-->
<jsp:include page="_footer.jsp" />
<!--/_footer /作为公共模版分离出去-->
<%--<script type="text/javascript" src="<c:url value="/static/h-ui.admin/js/H-ui.admin.page.js" />"></script>--%>
<script type="text/javascript">
	$(function (){
		getLangDate();
	});
	function getLangDate(){
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
		var newDate = year+"年"+month+"月"+date+"日 "+week+" "+hour+":"+minute+":"+second;
		document.getElementById("currTime").innerHTML = "刷新时间 ：[ "+newDate+" ]";
		// setTimeout("getLangDate()",1000);//每隔1秒重新调用一次该函数
	}
</script>

</body>
</html>