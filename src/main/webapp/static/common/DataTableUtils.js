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
            $("#dataStat").html(result.statDesc);
        },
        error : function(msg) {
        }
    });
}