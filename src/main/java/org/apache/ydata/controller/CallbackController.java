package org.apache.ydata.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.bot.BotStater;
import org.apache.ydata.bot.MyNotifyBot;
import org.apache.ydata.bot.MyUserBot;
import org.apache.ydata.utils.ResultGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@Controller(value = "CallbackOpenApi")
@RequestMapping(value = "/callback")
public class CallbackController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MyUserBot myUserBot;


    @Resource
    private BotStater botStater;

    @ResponseBody
    @RequestMapping(value = "/aliDmfNotify.htm")
    public Object aliDmfNotify() {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            log.info("aliDmfNotify paramName={}, paramValue={}", name, request.getParameter(name));
        }
        return ResultGenerator.success();
    }


    @ResponseBody
    @RequestMapping(value = "/yorder")
    public Object yorder() {
        String orderNo = request.getParameter("orderNo");
        if(ObjectUtils.isEmpty(orderNo)) return ResultGenerator.success();

        //user bot的相关信息
        if(redisTemplate.hasKey(orderNo)) {
            String chatAndMsgId = (String) redisTemplate.opsForValue().get(orderNo);
            String chatId = chatAndMsgId.split(":")[0];
            String msgId = chatAndMsgId.split(":")[1];
            myUserBot.replayCallbackQuery(chatId, msgId,  orderNo + " 已补单【系统消息】");
        }

        //notify bot 相关信息
        if(redisTemplate.hasKey("NOTIFY:" + orderNo)) {
            String chatAndMsgIdNotify = (String) redisTemplate.opsForValue().get("NOTIFY:" + orderNo);
            String notifyChatId = chatAndMsgIdNotify.split(":")[0];
            String notifyMsgId = chatAndMsgIdNotify.split(":")[1];
            //TODO 通知机器人回复：查单群消息
//            SendMessage message = new SendMessage();
//            message.setChatId(notifyChatId);
//            message.setReplyToMessageId(Integer.parseInt(notifyMsgId));
//            message.setParseMode("HTML");
//            message.setText("已补单【系统消息】");
//            try {
//                botStater.getNotifyBot().execute(message);
//            } catch (TelegramApiException e) {
//                log.error(e.getLocalizedMessage());
//            }
            //TODO 重新编辑机器人原消息，去掉内联菜单
            EditMessageCaption editMessageCaption = new EditMessageCaption();
            editMessageCaption.setChatId(notifyChatId);
            editMessageCaption.setMessageId(Integer.parseInt(notifyMsgId));
            editMessageCaption.setCaption(orderNo + " " + MyNotifyBot.CALLBACK_ORDER_SUCCESS_TEXT + "【系统消息】");
            try {
                botStater.getNotifyBot().execute(editMessageCaption);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return ResultGenerator.success();
    }


}
