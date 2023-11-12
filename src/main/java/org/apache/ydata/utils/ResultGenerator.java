package org.apache.ydata.utils;

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "成功";
    private static final String DEFAULT_FAIL_MESSAGE = "错误";

    public static Result success() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }
    public static Result success(ResultCode resultCode,Object data) {
        return new Result()
                .setCode(resultCode)
                .setData(data);
    }

    public static Result success(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result error(String message) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static Result error() {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(DEFAULT_FAIL_MESSAGE);
    }
    public static Result error(ResultCode resultCode) {
        return new Result()
                .setCode(resultCode);
    }

    public static Result error(ResultCode resultCode, String message) {
        return new Result()
                .setCode(resultCode)
                .setMessage(message);
    }

}
