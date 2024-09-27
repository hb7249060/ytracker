package org.apache.ydata.bot.mmutils;

import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.bot.MMBot;
import org.apache.ydata.service.RedisUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Slf4j
@Component
public class MMMsgSender {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisTemplate redisTemplate;

    private MMBot mmBot;

    public void setMmBot(MMBot mmBot) {
        this.mmBot = mmBot;
    }

    public void send(@NotNull String chatId, @NotNull String msg) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(msg)
                    .parseMode("HTML")
                    .build();
            this.mmBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendReply(Update update, String replyMsg) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .replyToMessageId(update.getMessage().getMessageId())
                    .text(replyMsg)
                    .parseMode("HTML")
                    .build();
            this.mmBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhotoReply(Update update, String photoUrl, String caption) {
        try {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .replyToMessageId(update.getMessage().getMessageId())
                    .photo(new InputFile(photoUrl))
                    .caption(caption)
                    .build();
            this.mmBot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
