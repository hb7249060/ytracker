package org.apache.ydata.utils;

import org.apache.ydata.constant.CoreConstant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String getClientIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个不为 unknown 的才为真实IP
            String[] ips = ip.split(",");
            for (String i : ips) {
                i = i.trim();
                if (!StringUtils.isEmpty(i) && !"unknown".equalsIgnoreCase(i)) {
                    ip = i;
                    break;
                }
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(":") > 0) {
            ip = request.getLocalAddr();
        }
        return ip;
    }

    public static HttpServletRequest getRequest() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        return null;
    }

    public static String getRequestIP() {
        HttpServletRequest request = getRequest();
        return request.getHeader(CoreConstant.HEADER_REQUEST_IP);
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static <T extends Annotation> T getAnnotation(JoinPoint point, Class<T> tClass) {
        return ((MethodSignature) point.getSignature()).getMethod().getAnnotation(tClass);
    }


    public static Map<String, Object> getFieldVlaue(Object obj) throws Exception {
        Map<String, Object> mapValue = new HashMap<String, Object>();
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            Method methodGet = cls.getDeclaredMethod(strGet);
            Object object = methodGet.invoke(obj);
            if (object instanceof Date) {
                object = DateUtil.parseDate((Date) object, DateEnum.PORTAL_DATE_PATTERN);
            }
            String value = object != null ? object.toString() : "";
            mapValue.put(name, value);
        }
        return mapValue;
    }

    public boolean isAjax() {
        if (!((getRequest().getHeader("accept") != null && getRequest().getHeader("accept").indexOf("application/json") > -1) ||
                (getRequest().getHeader("X-Requested-With") != null && getRequest().getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1) ||
                (getRequest().getHeader("Content-Type") != null && getRequest().getHeader("Content-Type").indexOf("multipart/form-data") > -1))) {
            return false;
        }
        return true;
    }

}
