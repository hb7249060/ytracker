package org.apache.ydata.bot;

import org.apache.ydata.service.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BotMsgReceiver {

    @Resource
    private RedisUtils redisUtils;

}
