function datetimeFormat(longTypeDate){
    var datetimeType = "";
    var date = new Date();
    date.setTime(longTypeDate);
    datetimeType+= date.getFullYear();
    datetimeType+= "-" + getMonth(date);
    datetimeType += "-" + getDay(date);
    datetimeType+= "&nbsp;" + getHours(date);
    datetimeType+= ":" + getMinutes(date);
    datetimeType+= ":" + getSeconds(date);
    return datetimeType;
}
function getMonth(date){
    var month = "";
    month = date.getMonth() + 1;
    if(month<10){
        month = "0" + month;
    }
    return month;
}
function getDay(date){
    var day = "";
    day = date.getDate();
    if(day<10){
        day = "0" + day;
    }
    return day;
}
function getHours(date){
    var hours = "";
    hours = date.getHours();
    if(hours<10){
        hours = "0" + hours;
    }
    return hours;
}
function getMinutes(date){
    var minute = "";
    minute = date.getMinutes();
    if(minute<10){
        minute = "0" + minute;
    }
    return minute;
}
function getSeconds(date){
    var second = "";
    second = date.getSeconds();
    if(second<10){
        second = "0" + second;
    }
    return second;
}

// 改变checkbox状态
function change_checkbox_state(ele, _checkbox_state, _style_state) {
    // disabled不可操作、checked已选中、unchecked不选中
    let white_state = ["checked", "disabled", "unchecked"];

    // 校验白名单
    let checkbox_state = "";
    if (white_state.includes(_checkbox_state)){
        checkbox_state = _checkbox_state;
    }else {
        console.log('checkbox_state值仅可取四个值：' + JSON.stringify(white_state) + "。默认checked");
        checkbox_state = "checked";
    }

    // 赋值
    let next_checkbox_state = "";
    let next_style_state = "";
    if (checkbox_state === "checked"){
        next_checkbox_state = "unchecked";
        next_style_state = "off";
    }else if(checkbox_state === "unchecked"){
        next_checkbox_state = "checked";
        next_style_state = "on";
    }else if (checkbox_state === "disabled") {
        next_checkbox_state = "disabled";
        next_style_state = "disabled";
    }else { // 默认关闭
        next_checkbox_state = "unchecked";
        next_style_state = "off";
    }

    // 更新参数
    ele.setAttribute("data-checkbox_state", next_checkbox_state);
    ele.setAttribute("data-style_state", next_style_state);
    ele.setAttribute(checkbox_state, checkbox_state);
}
// 点击某个checkbox
function click_checkbox(that, call_func, key, value) {
    // on打开，off关闭，disabled不可用
    let white_style_state = ["on", "off", "disabled"];
    let checkbox_state = that.getAttribute("data-checkbox_state");
    let style_state = that.getAttribute("data-style_state");
    if (!white_style_state.includes(style_state)){
        console.log('style_state值仅可取值：' + JSON.stringify(white_style_state) + "。默认为off");
        style_state = "off";
    }
    change_checkbox_state(that, checkbox_state, style_state);
    try {
        call_func(that, style_state, key, value);
    }catch (e) {
        console.error("未设置回调函数：checkbox_run(that, style_state, key, value)");
    }

}
// 初始化checkbox
function init_checkbox_state() {
    let checkbox = document.getElementsByClassName("checkbox-btn");
    for (let i=0; i<checkbox.length; i++){
        let ele = checkbox[i];
        let the_checkbox_state = ele.getAttribute("data-checkbox_state");
        let the_style_state = ele.getAttribute("data-style_state");
        change_checkbox_state(ele, the_checkbox_state, the_style_state);
    }
}

// (function () {
//     init_checkbox_state();
// })();