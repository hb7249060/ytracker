package org.apache.ydata.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class MyBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        log.info(update.toString());
        if (update.hasMessage() && update.getMessage().hasText()) {
            if(update.getMessage().getText().toString().equals("/chatid")) {
                sendChatId(update);
            } else if(update.getMessage().getText().toString().equals("/help")) {
                sendHelpInfo(update);
            }
        }
    }

    private void sendHelpInfo(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setReplyToMessageId(update.getMessage().getMessageId());
        message.setParseMode("HTML");
        StringBuffer msgText = new StringBuffer("您当前使用的是查单机器人\n");
        msgText.append("<b>使用方法：</b>\n")
                .append("<b>/help</b> 查看使用帮助\n")
                .append("<b>/chatid</b> 返回当前对话的ChatID，复制ChatId并在后台进行绑定\n");
        message.setText(msgText.toString());

        try {
            Message msg = execute(message);
            PinChatMessage pinChatMessage = new PinChatMessage();
            pinChatMessage.setMessageId(msg.getMessageId());
            pinChatMessage.setChatId(message.getChatId());
            try {
                execute(pinChatMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendChatId(Update update) {
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("当前对话ChatId是：" + update.getMessage().getChatId().toString() + "\n请使用此ChatId在系统中与您的账号绑定");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return Constants.botUsername;
    }

    @Override
    public String getBotToken() {
        return Constants.token;
    }
}
