package org.apache.ydata.service;

import org.apache.ydata.constant.RedisKeys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RedisUtils {

    @Resource
    private RedisTemplate redisTemplate;

    public String getSysConfig(String key) {
        Map systemProperties = (Map) redisTemplate.opsForValue().get(RedisKeys.SYSTEM_PROP);
        String value = systemProperties.containsKey(key) ?
                (String) systemProperties.get(key) : null;
        return value;
    }

}
