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
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 系统管理 <span class="c-gray en">&gt;</span> 三方支付通道绑定<a class="btn btn-success btn-refresh radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
<%--			<form id="form" action="<c:url value="/admin/business/userinfo/getList" />" method="post" >--%>
<%--				<div class="text-c"> 日期范围：--%>
<%--					<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" name="datemin" class="input-text Wdate" style="width:120px;">--%>
<%--					---%>
<%--					<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" name="datemax" class="input-text Wdate" style="width:120px;">--%>
<%--					<input type="text" class="input-text" style="width:150px" placeholder="输入账户名" id="ccname" name="searchKey1">--%>
<%--					<button type="button" onclick="search();" class="btn btn-success radius">--%>
<%--						<i class="Hui-iconfont">&#xe665;</i> 搜索账户--%>
<%--					</button>--%>
<%--					<button type="button" onclick="exportExcel();" class="btn btn-success radius"><i class="Hui-iconfont">&#xe644;</i> 按条件导出客户信息</button>--%>
<%--				</div>--%>
<%--			</form>--%>
			<div class="cl pd-5 bg-1 bk-gray mt-20">
				<span class="l">
<%--					<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>--%>
					<a href="javascript:;" onclick="member_add('添加绑定','<c:url value="/admin/config/hubpaychannel/addOrUpdate.html" />','1000','')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加绑定</a></span>
				<span class="r">
			</div>
			<div class="mt-20">
				<table class="table table-border table-bordered table-hover table-bg table-sort">
					<thead>
						<tr class="text-c">
<%--							<th width="20"><input type="checkbox" name="" value=""></th>--%>
<%--							<th width="20">ID</th>--%>
							<th width="40">ID</th>
							<th width="20">三方名称</th>
	<th width="20">系统通道</th>
	<th width="20">绑定商户</th>
	<th width="20">费率</th>
	<th width="10">权重</th>
	<th width="20">三方通道编码</th>
							<th width="20">状态</th>
							<th width="30">备注</th>
							<th width="50">创建时间</th>
							<th width="50">操作</th>
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
		{"data": "id","bSortable": false},
		{"data": "hubName","bSortable": false},
		{"data": "channelName","bSortable": false},
		{"data": "mchName","bSortable": false},
		{"data": "rate","bSortable": false},
		{"data": "weight","bSortable": false},
		{"data": "confPayCode","bSortable": false},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = "";
				if(data.usable == 0) {
					html += "<span style='color: #333; font-weight: bold'>停用</span>";
				} else if(data.usable == 1) {
					html += "<span style='color: #19a97b; font-weight: bold'>正常</span>";
				}
				return html;
		}},
		{"data": "memo","bSortable": false, "defaultContent":""},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
			return datetimeFormat(data.created);
		}},
		{"data": null, "className":"td-manage", "render": function (data, type, row, meta) {
			var itemid = data.id;
			var html = "<a title=\"编辑\" href=\"javascript:;\" onclick=\"member_edit('编辑','<c:url value="/admin/config/hubpaychannel/addOrUpdate.html" />?id=" + itemid + "','" + itemid + "', '1000','')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6df;</i>编辑</a>" +
					"\t\t<a title=\"测试下单\" href=\"javascript:;\" onclick=\"member_test(this, " + itemid + ")\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6e3;</i>测试下单</a>" +
					"\t\t<a title=\"删除\" href=\"javascript:;\" onclick=\"member_del(this, " + itemid + ")\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6e2;</i>删除</a>"
			return html;
		}}
	];
	var listAction = "<c:url value="/admin/config/hubpaychannel/getList" />";
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
/*用户-删除*/
function member_del(obj,id){
	layer.confirm('确认要删除吗？',function(index){
		layer.prompt({title: '输入您的安全密码', formType: 1}, function(pass, index2){
			var action = '<c:url value='/admin/config/hubpaychannel/deleteOne/'/>' + id;
			$.ajax({
				url: action,
				type: 'DELETE',
				data: 'secPwd=' + pass,
				success: function (data) {
					if(data.code == 200) {
						layer.msg(data.msg,{icon:1, time:1000});
						layer.close(index2);
						window.location.reload();
					} else {
						layer.msg(data.msg,{icon:5, time:1000});
					}
				},
				beforeSend: function (xhr) {
				},
				error: function (xhr, data, excption) {
				},
				complete: function (xhr, data) {
				}
			});
		});
	});
}

/*用户-测试下单*/
function member_test(obj,id){
	layer.confirm('确认要发起测试吗？',function(index){
		layer.prompt({title: '输入您的安全密码', formType: 1}, function(pass, index2){
			var action = '<c:url value='/admin/config/hubpaychannel/test/'/>' + id;
			$.ajax({
				url: action,
				type: 'POST',
				data: 'secPwd=' + pass,
				success: function (data) {
					if(data.code == 200) {
						layer.confirm('请求体：' + data.msg + '<br/>测试结果：' + JSON.stringify(data.data));
						// layer.msg(data.msg,{icon:1, time:1000});
						layer.close(index2);
						// window.location.reload();
					} else {
						layer.msg(data.msg,{icon:5, time:1000});
					}
				},
				beforeSend: function (xhr) {
				},
				error: function (xhr, data, excption) {
				},
				complete: function (xhr, data) {
				}
			});
		});
	});
}

</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>