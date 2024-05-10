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
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 业务管理 <span class="c-gray en">&gt;</span> 三方充值管理<a class="btn btn-success btn-refresh radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="Hui-article">
		<article class="cl pd-20">
			<form id="form" action="<c:url value="/admin/business/hubrechargerecord/getList" />" method="post" >
				<div class="text-l">
<%--					<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})" id="datemin" name="datemin" class="input-text Wdate" style="width:120px;">--%>
<%--					---%>
<%--					<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})" id="datemax" name="datemax" class="input-text Wdate" style="width:120px;">--%>
					<input type="text" class="input-text" style="width:150px" placeholder="三方" id="name" name="name" list="userNameList">
					<datalist id="userNameList"></datalist>
					<button type="button" onclick="search();" class="btn btn-success radius">
						<i class="Hui-iconfont">&#xe665;</i> 查询
					</button>
<%--					<button type="button" onclick="exportExcel();" class="btn btn-success radius"><i class="Hui-iconfont">&#xe644;</i> 按条件导出客户信息</button>--%>
				</div>
			</form>
			<div class="cl pd-5 bg-1 bk-gray mt-20">
				<span class="l">
<%--					<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>--%>
					<a href="javascript:;" onclick="member_add('添加三方系统费','<c:url value="/admin/business/hubrechargerecord/addOrUpdate.html" />','800','')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加三方系统费</a></span>
				</div>
			<div class="mt-20">
				<table class="table table-border table-bordered table-hover table-bg table-sort">
					<thead>
						<tr class="text-c">
<%--							<th width="20"><input type="checkbox" name="" value=""></th>--%>
							<th width="40">ID</th>
							<th width="30">三方信息</th>
							<th width="30">金额</th>
							<th width="30">备注</th>
							<th width="50">创建时间</th>
							<th width="40">操作</th>
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
<%--<script type="text/javascript" src="<c:url value="/static/common/DataTableUtils.js" />"></script>--%>
<script type="text/javascript" src="<c:url value="/static/lib/laypage/1.2/laypage.js" />"></script>
<script type="text/javascript">
	/**
	 * 公共参数，主要存放自定义的搜索条件数据
	 */
	var jsonCondition=null;
	/**
	 * datatable基本设置
	 * @sourceUrl 数据源Url
	 * @columns 列
	 */
	function dataTableInit(sourceUrl,columns,columnDefs,pageLength){
		$('.table-sort').DataTable({
			// "dom": '<"top"i>rt<"bottom"flp><"clear">',  //设置分页的位置
			"bProcessing": true,
			// 件数选择下拉框内容
			"lengthMenu": [10, 50, 75, 100,200],
			"iDisplayStart":0,
			// 每页的初期件数 用户可以操作lengthMenu上的值覆盖
			"pageLength": pageLength?pageLength:50,   //默认50
			"paging": true,//开启表格分页
			"bFilter": false,//去掉搜索框
			"processing": true, // 是否显示取数据时的那个等待提示
			"serverSide": true,//这个用来指明是通过服务端来取数据,如果不加
			"paginationType": "full_numbers",      //详细分页组，可以支持直接跳转到某页
			"ajaxSource": sourceUrl,//这个是请求的地址
			"serverData": retrieveData, // 获取数据的处理函数
			"bStateSave":true,
			// 每次创建是否销毁以前的DataTable,默认false
			"destroy": true,
			//   "autoWidth":true,//设置列宽自动
			"columns": columns,
			"columnDefs": columnDefs
		});
	}
	/**
	 * form表单序列化方法
	 */
	$.fn.serializeObject = function()
	{
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name]) {
				if (!o[this.name].push) {
					o[this.name] = [o[this.name]];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};

	/**
	 * 数据处理的方法
	 * @sSource datatable中设置的url
	 * @aoData 要传递到后台的数据主要是分页的信息
	 * @fnCallback 回调函数
	 */
	var page=new Object();
	function retrieveData( sSource,aoData, fnCallback) {

		$.each(aoData,function(index,item){
			if(item.name=="sEcho"){
				page.sEcho=item.value;
			}
			if (item.name=="iDisplayStart") {
				page.iDisplayStart=item.value;
			}
			if (item.name=="iDisplayLength") {
				page.iDisplayLength=item.value;
			}
		});
		if (jsonCondition) {
//将搜索条件和page拼接到一起
			$.extend(page,jsonCondition);
		}
		$.ajax({
			url : sSource,//这个就是请求地址对应sAjaxSource
			data : JSON.stringify(page),//这个是把datatable的一些基本数据传给后台,比如起始位置,每页显示的行数
			type : 'post',
			dataType : 'json',
			contentType:"application/json",
			//async : false,
			success : function(result) {   //后台执行成功的回调函数
				fnCallback(result);//把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
				// $("#dataStat").html(result.statDesc);
			},
			error : function(msg) {
			}
		});
	}
</script>
<script type="text/javascript">
$(function(){
	//列值定义,clientIP和count都是对应的返回对象的字段
	var colModel = [
		// {"data": "id","bSortable": false},
		{"data": "id","bSortable": false},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = data.hubName;
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = data.amount == null ? "" : data.amount;
				return data.amount;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
				var html = data.memo == null ? "" : data.memo;
				return html;
			}},
		{"data": null,"bSortable": false, "render": function (data, type, row, meta) {
			return datetimeFormat(data.created);
		}},
		{"data": null, "className":"td-manage", "render": function (data, type, row, meta) {
			var itemid = data.id;
			<%--var html = "<a title=\"编辑\" href=\"javascript:;\" onclick=\"member_edit('编辑','<c:url value="/admin/business/hubrechargerecord/addOrUpdate.html" />?id=" + itemid + "'," + itemid + ", '850','')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6df;</i>编辑</a>" +--%>
			<%--		"\t\t<a title=\"删除\" href=\"javascript:;\" onclick=\"member_del(this, " + itemid + ",'" + data.name + "')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6e2;</i>删除</a>";--%>

			return "";
		}}
	];
	var listAction = "<c:url value="/admin/business/hubrechargerecord/getList" />";
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
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>