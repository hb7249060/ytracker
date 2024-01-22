package org.apache.ydata.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.service.RedisUtils;
import org.apache.ydata.vo.SystemSettingsKeys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;

@Slf4j
@Component
public class BotMsgSender {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MyUserBot myUserBot;

    /**
     * 回复chatId信息 - 通知机器人
     * @param bot
     * @param update
     * @throws TelegramApiException
     */
    public void sendNotifyBotChatId(TelegramLongPollingBot bot, Update update) throws TelegramApiException {
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("当前对话ChatId是：" + update.getMessage().getChatId().toString() + "\n请使用此ChatId在系统中与您的账号绑定");
        bot.execute(message);
    }

    /**
     * 回复帮助信息 - 通知机器人
     * @param bot
     * @param update
     */
    public void sendNotifyBotHelpInfo(TelegramLongPollingBot bot, Update update) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setParseMode("HTML");
        StringBuffer msgText = new StringBuffer();
        msgText.append("<b>机器人使用方法：</b>\n")
                .append("<b>/help</b> 查看使用帮助\n")
                .append("<b>/chatid</b> 返回当前对话的ChatId，复制ChatId并在后台进行绑定\n");
        message.setText(msgText.toString());
        bot.execute(message);
    }

    /**
     * 转发报警信息 - 报警机器人
     * @param bot
     * @param update
     */
    public void redirectAlarmMsg(TelegramLongPollingBot bot, Update update) {
        String recvMsg = update.getMessage().getText();
        String newMsg = "<b>" + recvMsg.replaceAll("@" + bot.getBotUsername(), "").trim() + "</b>";
        newMsg += "\n\n--消息来源:" + update.getMessage().getFrom().getFirstName();
        String targetChatIds = redisUtils.getSysConfig(SystemSettingsKeys.ALARM_BOT_TARGET_CHAT_IDS);
        if(!ObjectUtils.isEmpty(targetChatIds)) {
            String[] chatIds = targetChatIds.replaceAll("\n", ",").split(",");
            for (String chatid : chatIds) {
                SendMessage message = new SendMessage();
                message.setChatId(chatid);
                message.setParseMode("HTML");
                message.setText(newMsg);
                try {
                    bot.execute(message);
                } catch (TelegramApiException e) {
                    log.error("error from chatid:{}", chatid);
                    log.error(e.getLocalizedMessage());
                }
            }
        }
    }

    /**
     * 发送内联菜单点击响应
     * @param bot 查单机器人
     * @param update 原消息[带callbackquery]
     * @param orderNo 目标订单
     * @param msg 目标内容
     */
    public void sendCallbackResult(TelegramLongPollingBot bot, Update update, String orderNo, String msg) {
        if(ObjectUtils.isEmpty(orderNo)) return;
        if(!redisTemplate.hasKey(orderNo)) {
            //超72小时过期的消息回执
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            message.setParseMode("HTML");
            message.setText("已过太久，讯息已失效，没办法帮您处理了");
            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getLocalizedMessage());
            }
            return;
        }
        String chatAndMsgId = (String) redisTemplate.opsForValue().get(orderNo);
        String chatId  = chatAndMsgId.split(":")[0];
        String msgId  = chatAndMsgId.split(":")[1];
        //TODO 机器人回复：查单群点击的消息
        SendMessage message = new SendMessage();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setReplyToMessageId(update.getCallbackQuery().getMessage().getMessageId());
        message.setParseMode("HTML");
        message.setText("处理结果：<b>" + msg + "</b>\n处理人：<b>" + update.getCallbackQuery().getFrom().getFirstName() + "</b>");
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getLocalizedMessage());
        }
        //TODO 重新编辑机器人原消息，去掉内联菜单
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setChatId(update.getCallbackQuery().getMessage().getChatId());
        editMessageCaption.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageCaption.setCaption(update.getCallbackQuery().getMessage().getCaption());
        try {
            bot.execute(editMessageCaption);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        //TODO 用户机器人回复：商户群原查单消息
        myUserBot.replayCallbackQuery(chatId, msgId, msg);
    }
}
