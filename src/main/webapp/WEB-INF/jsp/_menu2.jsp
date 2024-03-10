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
				<li <c:if test="${fn:contains(requestURL, '/admin/dashboard.html')}" > class="current" </c:if>>
					<a data-href="<c:url value='/admin/dashboard.html' /> " data-title="控制台" href="javascript:void(0)">控制台</a>
				</li>
			</dt>
		</dl>
		<dl id="menu-system4">
			<dt <c:if test="${fn:contains(requestURL, '/business/')}" > class="selected" </c:if>>
				<i class="Hui-iconfont">&#xe690;</i> 业务管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
			</dt>
			<dd <c:if test="${fn:contains(requestURL, '/syncer/')}" > style="display:block" </c:if>>
				<ul>
					<li <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
						<a data-href="<c:url value='/admin/business/hubinfo/list.html' /> " data-title="三方管理" href="javascript:void(0)"><i class="Hui-iconfont">&#xe60a;</i> 三方管理</a>
					</li>
					<li>
						<a data-href="<c:url value='/admin/business/hubrechargerecord/list.html' /> " data-title="三方加款管理" href="javascript:void(0)"><i class="Hui-iconfont">&#xe60a;</i> 三方加款管理</a>
					</li>
					<li <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
						<a data-href="<c:url value='/admin/business/stat/list.html' /> " data-title="三方数据统计" href="javascript:void(0)"><i class="Hui-iconfont">&#xe60a;</i> 三方数据统计</a>
					</li>
					<li <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
						<a data-href="<c:url value='/admin/business/hubgroup/list.html' /> " data-title="三方群组配置" href="javascript:void(0)"><i class="Hui-iconfont">&#xe60a;</i> 三方群组配置</a>
					</li>
					<li <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
						<a data-href="<c:url value='/admin/business/botaccount/list.html' /> " data-title="机器人信息库" href="javascript:void(0)"><i class="Hui-iconfont">&#xe60a;</i> 机器人信息库</a>
					</li>
				</ul>
			</dd>
		</dl>
		<dl id="menu-system5" <c:if test="${AdminUser.created != null}"> style="display: none;" </c:if>>
			<dt <c:if test="${fn:contains(requestURL, '/system/')}" > class="selected" </c:if>>
				<i class="Hui-iconfont">&#xe61d;</i> 系统管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
			</dt>
			<dd <c:if test="${fn:contains(requestURL, '/system/')}" > style="display:block" </c:if>>
				<ul>
					<li <c:if test="${fn:contains(requestURL, '/system/settings.jsp')}" > class="current" </c:if>>
						<a data-href="<c:url value='/admin/system/settings.html' /> " data-title="系统配置" href="javascript:void(0)"><i class="Hui-iconfont">&#xe63c;</i> 系统配置</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/admin/system/admin/list.html')}" > class="current" </c:if>>
						<a data-href="<c:url value='/admin/system/admin/list.html' /> " data-title="管理员管理" href="javascript:void(0)"><i class="Hui-iconfont">&#xe62b;</i> 管理员管理</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/admin/system/logs.html')}" > class="current" </c:if>>
						<a data-href="<c:url value='/admin/system/logs.html' /> " data-title="系统日志" href="javascript:void(0)"><i class="Hui-iconfont">&#xe623;</i> 系统日志</a>
					</li>
					<li <c:if test="${fn:contains(requestURL, '/index-pay.html')}" > class="current" </c:if>>
						<a data-href="<c:url value='/index-pay.html' /> " data-title="下单测试" href="javascript:void(0)"><i class="Hui-iconfont">&#xe623;</i> 下单测试</a>
					</li>
				</ul>
			</dd>
		</dl>
	</div>
</aside>
<div class="dislpayArrow hidden-xs"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>