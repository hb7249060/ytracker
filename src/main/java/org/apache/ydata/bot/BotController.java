package org.apache.ydata.bot;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.utils.ResultGenerator;
import org.apache.ydata.utils.Tools;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/bot")
public class BotController {

    @PostMapping(value = "/sendMsg")
    public Object sendMsg(String type,
                          String chatId,
                          String jsonBody) {
        DefaultAbsSender defaultAbsSender = new DefaultAbsSender(new DefaultBotOptions()) {
            @Override
            public String getBotToken() {
                return Constants.token;
            }
        };
        switch (type) {
            case "NewOrder" :
                //新订单
                SendMessage message1 = new SendMessage(); // Create a SendMessage object with mandatory fields
                message1.setChatId(chatId);
                message1.setText("新订单通知：\n" + jsonBody);
                try {
                    defaultAbsSender.execute(message1);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "QueryOrder" :
                //查单
                //解析order
                OrderVo order = JSONObject.parseObject(jsonBody, OrderVo.class);
                //发送查单消息
                SendMessage message2 = new SendMessage(); // Create a SendMessage object with mandatory fields
                message2.setChatId(chatId);
                StringBuffer msgText = new StringBuffer("<b>查单通知：</b>\n");
                msgText.append("交易号：" + order.getTradeNo());

                msgText.append("\n");
                msgText.append("订单号：" + order.getOrderNo());

                msgText.append("\n");
                msgText.append("金额：<b>" + new BigDecimal(order.getFee()).setScale(2, BigDecimal.ROUND_UP).toString() + "</b>");

                msgText.append("\n");
                msgText.append("收款码别名：" + order.getPayCodeAlias());
//                String orderStateText = "";
//                if(order.getOrderState() == 0) {
//                    orderStateText = "支付中";
//                } else if(order.getOrderState() == 1) {
//                    orderStateText = "已完成";
//                } else if(order.getOrderState() == -1) {
//                    orderStateText = "支付失败";
//                }
//                msgText.append("订单状态：" + orderStateText  + "\n");

                try {
                    msgText.append("\n");
                    msgText.append("订单生成时间：" + Tools.timeDateString(order.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
//                String notifyStateText = "";
//                if(order.getNotifyState() == 0) {
//                    notifyStateText = "未通知";
//                } else if(order.getNotifyState() == 1) {
//                    notifyStateText = "已通知";
//                }
//                msgText.append("通知状态：" + notifyStateText + "\n");
                if(!ObjectUtils.isEmpty(order.getMemo())) {
                    msgText.append("\n");
                    msgText.append("<b>" + order.getMemo() + "</b>");
                }

//                message2.setText(escape(msgText.toString()));
                message2.setText(msgText.toString());
                message2.setParseMode("HTML");
                try {
                    defaultAbsSender.execute(message2);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    return ResultGenerator.error("机器人查单消息推送失败：" + e.getMessage());
                }
                break;
            default:
                break;
        }
        return ResultGenerator.success();
    }

    public String escape(String s) {
        if (s == null) return null;
        List<String> list = Arrays.asList("#", "!", "*", "_", "~", "+", "-", ">", "<", "[", "]", "`", ".", "^", "=");
        for (String l : list) {
            s = s.replace(l, "\\" + l);
        }
        return s;
    }

    @GetMapping(value = "/testmsg/{chatId}/{msg}")
    public Object testMsg(@PathVariable String chatId, @PathVariable String msg) {
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(chatId);
        message.setText(msg);
        DefaultAbsSender defaultAbsSender = new DefaultAbsSender(new DefaultBotOptions()) {
            @Override
            public String getBotToken() {
                return Constants.token;
            }
        };
        try {
            defaultAbsSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return ResultGenerator.success();
    }
}
