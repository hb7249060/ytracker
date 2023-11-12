package org.apache.ydata.utils;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -2113706917862971597L;
    private ResultCode code;

    public ServiceException(ResultCode code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ResultCode code) {
        this.code = code;
    }

    public ServiceException(String message) {
        super(message);
        this.code = ResultCode.INTERNAL_SERVER_ERROR;
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }
}
