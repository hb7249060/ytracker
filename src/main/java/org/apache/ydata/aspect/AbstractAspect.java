package org.apache.ydata.aspect;

import com.alibaba.fastjson2.JSON;
import org.apache.ydata.utils.CommonUtil;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public abstract class AbstractAspect {
    private static final Set<String> USELESS_HEADER = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("referer", "accept", "accept-language", "cookie", "host",
                    "connection", "accept-encoding", "user-agent")));

    public void log(Logger logger, HttpServletRequest request) {
        StringBuffer headerBuffer = new StringBuffer();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String nextElement = headerNames.nextElement();
            if (!USELESS_HEADER.contains(nextElement)) {
                String value = request.getHeader(nextElement);
            }
        }
        logger.info("请求接口：{}，请求IP：{}，请求参数：{}，请求头：{}",
                request.getRequestURI(), CommonUtil.getClientIp(),
                JSON.toJSONString(request.getParameterMap()), headerBuffer.toString());
    }

    public abstract boolean getLogin();

    protected Map<String,Object> getDataFromRequest(HttpServletRequest request){
        Map<String,Object> receiveMap = new HashMap<String,Object>();
        String appKey = request.getHeader(HeaderKeys.AppKey);
        String nonce = request.getHeader(HeaderKeys.Nonce);
        String curTime = request.getHeader(HeaderKeys.CurTime);
        String checkSum = request.getHeader(HeaderKeys.CheckSum);
        String clientIp = CommonUtil.getClientIp();
        receiveMap.put(HeaderKeys.AppKey, appKey);
        receiveMap.put(HeaderKeys.Nonce, nonce);
        receiveMap.put(HeaderKeys.CurTime, curTime);
        receiveMap.put(HeaderKeys.CheckSum, checkSum);
        receiveMap.put("ClientIp", clientIp);
        receiveMap.put("RequestURI", request.getRequestURI());
//        try {
//            BufferedReader bufferedReader = request.getReader();
//            String bodyStr = bufferedReader.readLine();
//            receiveMap.put("Body", bodyStr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return receiveMap;
    }

}
