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
//        log.info(update.toString());
//        String captionText = update.getMessage().getCaption();
//        if(captionText.contains("商户订单号") && captionText.contains("系统订单号")) {
//            captionText = captionText.substring(captionText.indexOf("系统订单号") + "系统订单号".length(),
//                    captionText.indexOf("订 单  金 额")).replace(":", "").replace("：", "").trim();
//            log.info("get MessagePhoto content7: " + captionText);
//        }
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
        } else if(data.startsWith(CALLBACK_ORDER_OTHER_EXCPTION)) {
            //其它异常
            msg = CALLBACK_ORDER_OTHER_EXCPTION_TEXT;
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

    public static final String CALLBACK_ORDER_SUCCESS = "order:success:";
    public static final String CALLBACK_ORDER_SUCCESS_TEXT = "已补单";
    public static final String CALLBACK_ORDER_FAIL = "order:fail:";
    public static final String CALLBACK_ORDER_FAIL_TEXT = "未付款";
    public static final String CALLBACK_ORDER_MISMATCH = "order:mismatch:";
    public static final String CALLBACK_ORDER_MISMATCH_TEXT = "单图不符";
    public static final String CALLBACK_ORDER_MODIFYFEE = "order:modifyfee:";
    public static final String CALLBACK_ORDER_MODIFYFEE_TEXT = "修改金额";
    public static final String CALLBACK_ORDER_OTHER_EXCPTION = "order:otherexception:";
    public static final String CALLBACK_ORDER_OTHER_EXCPTION_TEXT = "其它异常";
    public static final String DEFAULT_ORDER_NO = "0123456789ABCDEFG";

    public void sendImageText(String localFilepath, String captionText, Long notifyChatId, String orderNo) {
        mBotMsgSender.sendNotifyImageText(this, localFilepath, captionText, notifyChatId, orderNo);
    }
}
