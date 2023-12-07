package org.apache.ydata.bot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * 系统告警机器人
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class MyAlarmBot extends TelegramLongPollingBot {

    private BotMsgReceiver mBotMsgReceiver;

    private BotMsgSender mBotMsgSender;

    private String username;
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
//        log.info(update.toString());
        if (update.hasMessage() && update.getMessage().hasText()) {
            String recvMsg = update.getMessage().getText();
            if(recvMsg.equals("/chatid@" + username)) {
                sendChatId(update);
            } else if(recvMsg.equals("/help@" + username)) {
                sendHelpInfo(update);
            } else if(recvMsg.contains("@" + username)) {
                //消息直接转发
                mBotMsgSender.redirectAlarmMsg(this, update);
            }
        }
    }

    private void sendHelpInfo(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
//        message.setReplyToMessageId(update.getMessage().getMessageId());
        message.setParseMode("HTML");
        StringBuffer msgText = new StringBuffer();
        msgText.append("<b>机器人使用方法：</b>\n")
                .append("<b>/help</b> 查看使用帮助\n")
                .append("<b>/chatid</b> 返回当前对话的ChatId\n");
        message.setText(msgText.toString());

        try {
            Message msg = execute(message);
//            PinChatMessage pinChatMessage = new PinChatMessage();
//            pinChatMessage.setMessageId(msg.getMessageId());
//            pinChatMessage.setChatId(message.getChatId());
//            try {
//                execute(pinChatMessage);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendChatId(Update update) {
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("当前对话ChatId是：" + update.getMessage().getChatId().toString());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
