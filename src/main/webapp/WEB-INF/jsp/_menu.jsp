<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
	String requestURL = request.getRequestURL().toString();
%>

<c:set var="requestURL" value="${pageContext.request.requestURL}" />

<aside class="Hui-aside">
	<div class="menu_dropdown bk_2">
		<dl id="menu-system">
			<dt <c:if test="${fn:contains(requestURL, '/admin/index.html')}" > class="selected" </c:if>>
				<li <c:if test="${fn:contains(requestURL, '/admin/index.html')}" > class="current" </c:if>>
					<a href="<c:url value='/admin/index.html' /> " title="控制台">控制台</a>
				</li>
			</dt>
		</dl>
		<dl id="menu-system2">
			<dt <c:if test="${fn:contains(requestURL, '/business/')}" > class="selected" </c:if>>
				<i class="Hui-iconfont">&#xe637;</i> 业务管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
			</dt>
			<dd <c:if test="${fn:contains(requestURL, '/business/')}" > style="display:block" </c:if>>
				<ul>
					<li <c:if test="${fn:contains(requestURL, '/business/orderinfo/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/business/orderinfo/list.html' /> " title="订单管理"><i class="Hui-iconfont">&#xe687;</i> 订单管理</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/business/merchant/po_record_list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/business/merchant/po_record_list.html' /> " title="订单管理"><i class="Hui-iconfont">&#xe623;</i> 商户下单记录</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/business/userinfo/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/business/userinfo/list.html' /> " title="码商管理"><i class="Hui-iconfont">&#xe60a;</i> 码商管理</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/business/paycode/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/business/paycode/list.html' /> " title="收款码管理"><i class="Hui-iconfont">&#xe72d;</i> 收款码管理</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/business/userpoints/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/business/userpoints/list.html' /> " title="积分管理"><i class="Hui-iconfont">&#xe6b5;</i> 积分管理</a>
					</li>
				</ul>
			</dd>
		</dl>
		<dl id="menu-system3">
			<dt <c:if test="${fn:contains(requestURL, '/systemstat/')}" > class="selected" </c:if>>
				<i class="Hui-iconfont">&#xe61e;</i> 统计<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
			</dt>
			<dd <c:if test="${fn:contains(requestURL, '/systemstat/')}" > style="display:block" </c:if>>
				<ul>
					<li <c:if test="${fn:contains(requestURL, '/systemstat/userinfo/stat.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/systemstat/userinfo/stat.html' /> " title="业绩统计"><i class="Hui-iconfont">&#xe61a;</i> 码商日终</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/systemstat/merchant/stat.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/systemstat/merchant/stat.html' /> " title="业绩统计"><i class="Hui-iconfont">&#xe621;</i> 商户日终</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/systemstat/stat.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/systemstat/stat.html' /> " title="业绩统计"><i class="Hui-iconfont">&#xe61c;</i> 业绩统计</a>
					</li>
				</ul>
			</dd>
		</dl>
		<dl id="menu-system4">
			<dt <c:if test="${fn:contains(requestURL, '/syncer/')}" > class="selected" </c:if>>
				<i class="Hui-iconfont">&#xe690;</i> 账单同步器<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
			</dt>
			<dd <c:if test="${fn:contains(requestURL, '/syncer/')}" > style="display:block" </c:if>>
				<ul>
					<li <c:if test="${fn:contains(requestURL, '/syncer/robot/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/syncer/robot/list.html' /> " title="UID机器人账号"><i class="Hui-iconfont">&#xe6a2;</i> UID机器人账号</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/syncer/alipayorder/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/syncer/alipayorder/list.html' /> " title="UID账单"><i class="Hui-iconfont">&#xe71c;</i> UID账单</a>
					</li>
				</ul>
			</dd>
		</dl>
		<dl id="menu-system5">
			<dt <c:if test="${fn:contains(requestURL, '/system/')}" > class="selected" </c:if>>
				<i class="Hui-iconfont">&#xe61d;</i> 系统管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
			</dt>
			<dd <c:if test="${fn:contains(requestURL, '/system/')}" > style="display:block" </c:if>>
				<ul>
					<li <c:if test="${fn:contains(requestURL, '/system/settings.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/system/settings.html' /> " title="系统配置"><i class="Hui-iconfont">&#xe63c;</i> 系统配置</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/system/merchant/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/system/merchant/list.html' /> " title="系统配置"><i class="Hui-iconfont">&#xe66a;</i> 商户管理</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/system/paychannel/list.jsp')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/system/paychannel/list.html' /> " title="系统配置"><i class="Hui-iconfont">&#xe628;</i> 支付通道</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/admin/system/admin/list.html')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/system/admin/list.html' /> " title="管理员管理"><i class="Hui-iconfont">&#xe62b;</i> 管理员管理</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/admin/system/logs.html')}" > class="current" </c:if>>
						<a href="<c:url value='/admin/system/logs.html' /> " title="系统日志"><i class="Hui-iconfont">&#xe623;</i> 系统日志</a>
					</li>
				</ul>
			</dd>
		</dl>
	</div>
</aside>
<div class="dislpayArrow hidden-xs"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>