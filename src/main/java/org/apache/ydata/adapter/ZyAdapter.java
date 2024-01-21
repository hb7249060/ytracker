package org.apache.ydata.adapter;

import com.alibaba.fastjson2.JSONObject;
import com.beust.jcommander.internal.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.utils.HttpUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Slf4j
@Configuration
public class ZyAdapter implements BaseAdapter {

//    @Resource
//    private RedisTemplate redisTemplate;

    @Override
    public JSONObject stat(HubInfo hubInfo, String statDate) {
        //发起同步http请求，拉取订单
        String Nonce = String.valueOf(System.currentTimeMillis());
        String CurTime = String.valueOf(System.currentTimeMillis() / 1000);

        Map dataMap = Maps.newHashMap();
        dataMap.put("startDate", statDate); //yyyy-MM-dd格式
        dataMap.put("endDate", statDate);
        //这里使用自己系统的notifyUrl，不暴露商户给的NotifyUrl
        String notifyUrl = null;
//        Map systemProperties = (Map) redisTemplate.opsForValue().get(RedisKeys.SYSTEM_PROP);
//        if(!ObjectUtils.isEmpty(systemProperties) && systemProperties.containsKey(SystemSettingsKeys.NOTIFY_URL)) {
//            notifyUrl = systemProperties.get(SystemSettingsKeys.NOTIFY_URL).toString();
//        }
        if(!ObjectUtils.isEmpty(notifyUrl)) {
            dataMap.put("notifyUrl", notifyUrl);
        }

        JSONObject result = HttpUtils.post(hubInfo.getApiUrl() + "/statData", null, dataMap);
        return result;
    }

    @Override
    public JSONObject getBindChatId(HubInfo hubInfo, String orderNo) {
        Map dataMap = Maps.newHashMap();
        dataMap.put("tradeNo", orderNo); //yyyy-MM-dd格式

        JSONObject result = HttpUtils.post(hubInfo.getApiUrl() + "/getBindChatId", null, dataMap);
        return result;
    }

}
