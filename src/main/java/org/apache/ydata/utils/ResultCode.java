package org.apache.ydata.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {

    SUCCESS(200, "成功"),//成功
    BIND_GOOGLE_CODE(205, "需绑定google验证器"),  //需绑定google验证器
    SUCCESS_PART(206, "响应成功,返回部分信息"),//响应成功,返回部分信息
    IP_BLOCKED(301, "IP被封禁"),//IP被封禁
    IP_LIMITED(315, "IP限制"),//IP限制
    FAIL(400, "失败"),//失败
    UNAUTHORIZED(401, "验签失败"),//未认证（签名错误）
    NOT_FOUND(404, "Not Found"),//接口不存在
    REQUEST_TIMEOUT(408, "请求超时"),//请求超时
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),//服务器内部错误
    SALE_BUY_CHANGE_CODE(310, "服务器内部错误"),//服务器内部错误
    BASE_PARAM_ERR_CODE(414, "参数错误"),//参数校验不通过 自己定义的errodcode
    QPS_LIMITED(416, "频率控制"),//频率控制
    ACCOUNT_DISABLED(422, "账号被禁用"),//账号被禁用
    BASE_BAD_REQUEST_ERR_CODE(462, "参数校验不通过"),//参数校验不通过 自己定义的errodcode
    FREZEE_ACOUNT_ERROR(9, ""),
    ACOUNT_IS_REVIEWING(209, ""),
    BUSINESS_FAULT(302, "业务异常，用户不存在或已被停用"),//业务异常
    ACOUNT_IS_REFUSE(210, "请求被拒绝，请重新登录"),
    BALANCE_NOT_ENOUGHT(801, "余额不足");   //余额不足

    public static List getAllEventType() {
        List list = new ArrayList<>(values().length);
        for (ResultCode enumList : values()) {
            list.add(enumList);
        }
        return list;
    }

    public static List getAllCode() {
        List list = new ArrayList<>(values().length);
        for (ResultCode enumList : values()) {
            list.add(enumList.getCode());
        }
        return list;
    }

    public static String getDescByType(String type) {
        for (ResultCode enumList : values()) {
            if(type.equals(enumList.getCode())) {
                return enumList.desc;
            }
        }
        return null;
    }


    public int getCode() {
        return code;
    }
    public String getDesc() {
        return this.desc;
    }

    ResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final int code;
    private final String desc;


}
