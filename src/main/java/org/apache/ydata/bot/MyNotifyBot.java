package org.apache.ydata.bot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通知机器人，用于单人对话
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class MyNotifyBot extends TelegramLongPollingBot {
    private BotMsgReceiver mBotMsgReceiver;
    private BotMsgSender mBotMsgSender;

    private String username;
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        log.info(update.toString());
        if(update.hasCallbackQuery()) {
            //处理回调
            processCallbackQuery(update);
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String recvMsg = update.getMessage().getText();
            if(recvMsg.contains("chatid") || recvMsg.contains("chatid @" + username) || recvMsg.contains("chatid@" + username)) {
                try {
                    mBotMsgSender.sendNotifyBotChatId(this, update);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(recvMsg.contains("help") || recvMsg.contains("help@" + username) || recvMsg.contains("help @" + username)) {
                try {
                    mBotMsgSender.sendNotifyBotHelpInfo(this, update);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //处理内联菜单点击事件，callback query
    private void processCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();
        String orderNo = data.split(":")[2];
        String msg = null;
        if(data.startsWith(CALLBACK_ORDER_SUCCESS)) {
            //成功
            msg = CALLBACK_ORDER_SUCCESS_TEXT;
        } else if(data.startsWith(CALLBACK_ORDER_FAIL)) {
            //已退款
            msg = CALLBACK_ORDER_FAIL_TEXT;
        } else if(data.startsWith(CALLBACK_ORDER_MISMATCH)) {
            //单图不符
            msg = CALLBACK_ORDER_MISMATCH_TEXT;
        } else if(data.startsWith(CALLBACK_ORDER_MODIFYFEE)) {
            //修改金额
            msg = CALLBACK_ORDER_MODIFYFEE_TEXT;
        }
        if(!DEFAULT_ORDER_NO.equals(orderNo)) {
            mBotMsgSender.sendCallbackResult(this, update, orderNo, msg);
        }
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    private static final String CALLBACK_ORDER_SUCCESS = "order:success:";
    private static final String CALLBACK_ORDER_SUCCESS_TEXT = "已补单";
    private static final String CALLBACK_ORDER_FAIL = "order:fail:";
    private static final String CALLBACK_ORDER_FAIL_TEXT = "未付款";
    private static final String CALLBACK_ORDER_MISMATCH = "order:mismatch:";
    private static final String CALLBACK_ORDER_MISMATCH_TEXT = "单图不符";
    private static final String CALLBACK_ORDER_MODIFYFEE = "order:modifyfee:";
    private static final String CALLBACK_ORDER_MODIFYFEE_TEXT = "修改金额";
    private static final String DEFAULT_ORDER_NO = "0123456789ABCDEFG";

    public void sendImageText(String localFilepath, String captionText, Long notifyChatId, String orderNo) {
        if(ObjectUtils.isEmpty(orderNo)) {
            orderNo = DEFAULT_ORDER_NO;   //兼容旧代码防止order为空情况
        }
        //内联菜单
        InlineKeyboardButton button1 = InlineKeyboardButton.builder().text(CALLBACK_ORDER_SUCCESS_TEXT).callbackData(CALLBACK_ORDER_SUCCESS + orderNo).build();
        InlineKeyboardButton button2 = InlineKeyboardButton.builder().text(CALLBACK_ORDER_FAIL_TEXT).callbackData(CALLBACK_ORDER_FAIL + orderNo).build();
        InlineKeyboardButton button3 = InlineKeyboardButton.builder().text(CALLBACK_ORDER_MISMATCH_TEXT).callbackData(CALLBACK_ORDER_MISMATCH + orderNo).build();
        InlineKeyboardButton button4 = InlineKeyboardButton.builder().text(CALLBACK_ORDER_MODIFYFEE_TEXT).callbackData(CALLBACK_ORDER_MODIFYFEE + orderNo).build();
        List<InlineKeyboardButton> list1 = new ArrayList<>();
        list1.add(button1);
        list1.add(button2);
        List<InlineKeyboardButton> list2 = new ArrayList<>();
        list2.add(button3);
        list2.add(button4);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Collections.addAll(rowList, list1, list2);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(rowList).build();

        //图文消息
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(new File(localFilepath)));
        sendPhoto.setCaption(captionText);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        sendPhoto.setChatId(notifyChatId);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
