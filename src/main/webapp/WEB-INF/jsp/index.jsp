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
<jsp:include page="_header.jsp" />
<!--/_header 作为公共模版分离出去-->

<!--_menu 作为公共模版分离出去-->
<jsp:include page="_menu.jsp" />
<!--/_menu 作为公共模版分离出去-->

<section class="Hui-article-box">
	<nav class="breadcrumb"><i class="Hui-iconfont"></i> <a href="/" class="maincolor">首页</a>
		<span class="c-999 en">&gt;</span>
		<span class="c-666">控制台</span>
		<a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<p class="f-20 text-success">欢迎来到 <b>银河支付</b>
<%--				<span class="f-14">v2.0</span>--%>
			</p>
			<p><hr></p>
			<table class="table table-border table-bordered table-hover table-bg table-sort">
				<tr>
					<td>码商数量</td>
					<td class="warning">${data.userCount}</td>
				</tr>
				<tr>
					<td>收款码数量</td>
					<td>${data.payCodeCount}</td>
				</tr>
				<tr>
					<td>可用收款码数量</td>
					<td class="success">${data.payCodeAvaliCount}</td>
				</tr>
				<tr>
					<td>码商总积分</td>
					<td class="success">${data.totalPoints}</td>
				</tr>
			</table>
			<p><hr></p>
			<table class="table table-border table-bordered table-hover table-bg table-sort">
				<thead>
					<tr>
						<th colspan="2" scope="col">今日统计</th>
					</tr>
					<tr>
						<th>统计项</th>
						<th>内容</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>码商充值总积分</td>
						<td class="danger">￥${data.rechargePoints}</td>
					</tr>
					<tr>
						<td>订单总数</td>
						<td>
							<span style="line-height: 40px;">${data.orderTotalCount}</span>
							<table class="table table-border table-bordered table-hover table-bg table-sort">
								<c:forEach items="${data.payChannelStat}" var="item">
									<tr>
										<td width="200">${item.channelName}</td>
										<td>${item.orderTotalCount}</td>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					<tr>
						<td>支付成功订单数</td>
						<td>
							<span style="line-height: 40px;">${data.orderTotalPayCount}</span>
							<table class="table table-border table-bordered table-hover table-bg table-sort">
								<c:forEach items="${data.payChannelStat}" var="item">
									<tr>
										<td width="200">${item.channelName}</td>
										<td>${item.orderTotalPayCount}</td>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					<tr>
						<td>订单总金额</td>
						<td>
							<span style="line-height: 40px;">￥${data.orderTotalFee}</span>
							<table class="table table-border table-bordered table-hover table-bg table-sort">
								<c:forEach items="${data.payChannelStat}" var="item">
									<tr>
										<td width="200">${item.channelName}</td>
										<td>￥${item.orderTotalFee}</td>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					<tr>
						<td>支付成功订单总金额</td>
						<td>
							<span style="line-height: 40px;">￥${data.orderPayFee}</span>
							<table class="table table-border table-bordered table-hover table-bg table-sort">
								<c:forEach items="${data.payChannelStat}" var="item">
									<tr>
										<td width="200">${item.channelName}</td>
										<td>￥${item.orderPayFee}</td>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					<tr>
						<td>支付成功率</td>
						<td>
<%--							<span style="line-height: 40px;">${data.orderSuccessRate}</span>--%>
							<table class="table table-border table-bordered table-hover table-bg table-sort">
								<c:forEach items="${data.payChannelStat}" var="item">
									<tr>
										<td width="200">${item.channelName}</td>
										<td>${item.orderSuccessRate}%</td>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
			<p><hr></p>
			<table class="table table-border table-bordered table-hover table-bg table-sort">
				<thead>
				<tr>
					<th colspan="2" scope="col">昨日统计</th>
				</tr>
				<tr>
					<th>统计项</th>
					<th>内容</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>码商充值总积分</td>
					<td class="danger">￥${data.rechargePoints2}</td>
				</tr>
				<tr>
					<td>订单总数</td>
					<td>
						<span style="line-height: 40px;">${data.orderTotalCount2}</span>
						<table class="table table-border table-bordered table-hover table-bg table-sort">
							<c:forEach items="${data.payChannelStat2}" var="item">
								<tr>
									<td width="200">${item.channelName}</td>
									<td>${item.orderTotalCount}</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<td>支付成功订单数</td>
					<td>
						<span style="line-height: 40px;">${data.orderTotalPayCount2}</span>
						<table class="table table-border table-bordered table-hover table-bg table-sort">
							<c:forEach items="${data.payChannelStat2}" var="item">
								<tr>
									<td width="200">${item.channelName}</td>
									<td>${item.orderTotalPayCount}</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<td>订单总金额</td>
					<td>
						<span style="line-height: 40px;">￥${data.orderTotalFee2}</span>
						<table class="table table-border table-bordered table-hover table-bg table-sort">
							<c:forEach items="${data.payChannelStat2}" var="item">
								<tr>
									<td width="200">${item.channelName}</td>
									<td>${item.orderTotalFee}</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<td>支付成功订单总金额</td>
					<td>
						<span style="line-height: 40px;">￥${data.orderPayFee2}</span>
						<table class="table table-border table-bordered table-hover table-bg table-sort">
							<c:forEach items="${data.payChannelStat2}" var="item">
								<tr>
									<td width="200">${item.channelName}</td>
									<td>${item.orderPayFee}</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr>
					<td>支付成功率</td>
					<td>
<%--						<span style="line-height: 40px;">￥${data.orderSuccessRate2}</span>--%>
						<table class="table table-border table-bordered table-hover table-bg table-sort">
							<c:forEach items="${data.payChannelStat2}" var="item">
								<tr>
									<td width="200">${item.channelName}</td>
									<td>${item.orderSuccessRate}%</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				</tbody>
			</table>
</article>
</div>
</section>

<!--_footer 作为公共模版分离出去-->
<jsp:include page="_footer.jsp" />
<!--/_footer /作为公共模版分离出去-->
<script type="text/javascript" src="<c:url value="/static/h-ui.admin/js/H-ui.admin.page.js" />"></script>


</body>
</html>