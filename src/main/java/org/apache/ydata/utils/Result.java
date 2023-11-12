package org.apache.ydata.utils;


import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一API响应结果封装
 */
public class Result<T> {
    private int code;
    private String message;
    private Long traceLogId;
    private Object data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp = new Date();

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getDesc();
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Long getTraceLogId() {
        return traceLogId;
    }

    public void setTraceLogId(Long traceLogId) {
        this.traceLogId = traceLogId;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    @SuppressWarnings("unchecked")
    public Result putData(String key, Object value) {
        if (null == this.data) {
            this.data = new HashMap<String, Object>();
        }
        if (this.data instanceof Map) {
            Map<String, Object> map = ((Map) this.data);
            map.put(key, value);
        }
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public Boolean success() {
        return this.code - ResultCode.SUCCESS.getCode() == 0;
    }
}
