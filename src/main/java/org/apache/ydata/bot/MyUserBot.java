package org.apache.ydata.bot;

import com.alibaba.fastjson2.JSONObject;
import it.tdlight.Init;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.hub.BotAccount;
import org.apache.ydata.model.hub.HubGroup;
import org.apache.ydata.model.hub.HubGroupBot;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.BotAccountService;
import org.apache.ydata.service.hub.HubGroupBotService;
import org.apache.ydata.service.hub.HubGroupService;
import org.apache.ydata.service.hub.HubInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class MyUserBot {

    @Value("${config.telegram.userBot.api_id}")
    private int userBotApiId;

    @Value("${config.telegram.userBot.api_hash}")
    private String userBotApiHash;

    @Value("${config.telegram.userBot.phone_number}")
    private String userBotPhoneNumber;

    @Value("${config.telegram.userBot.user_id}")
    private long userBotUserId;

    @Value("${config.telegram.userBot.username}")
    private String userBotUsername;

    @Value("${config.telegram.userBot.admin_id}")
    private long userBotAdminId;

    @Value("${config.telegram.tdlibPath}")
    private String tdlibPath;

    private SimpleTelegramClient client;

    @Resource
    private BotAccountService botAccountService;

    @Resource
    private HubGroupBotService hubGroupBotService;

    @Resource
    private HubGroupService hubGroupService;

    @Resource
    private HubInfoService hubInfoService;

    @Resource
    private ZyAdapter zyAdapter;

    private BotStater botStater;

    public void init(BotStater botStater) throws Exception {
        this.botStater = botStater;
        System.setProperty("java.library.path", tdlibPath);
//        System.out.println(System.getProperty("java.library.path"));
        Init.init();
        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()) {
            APIToken apiToken = new APIToken(userBotApiId, userBotApiHash);
            TDLibSettings settings = TDLibSettings.create(apiToken);
            Path sessionPath = Paths.get("teluserbot");
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

            SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);
            SimpleAuthenticationSupplier<?> authenticationData = AuthenticationSupplier.user(userBotPhoneNumber);

            setupHandlers(clientBuilder);
            client = clientBuilder.build(authenticationData);
            client.waitForExit();

        }
    }

    private void setupHandlers(SimpleTelegramClientBuilder clientBuilder) {
        clientBuilder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, updateAuthorizationState -> {
            onUpdateAuthorizationState(updateAuthorizationState);
        });
        clientBuilder.addUpdateHandler(TdApi.UpdateNewMessage.class, updateNewMessage -> {
            onUpdateNewMessage(updateNewMessage);
        });
    }

    private boolean isAdmin(TdApi.MessageSender sender) {
        return sender instanceof TdApi.MessageSenderUser && ((TdApi.MessageSenderUser) sender).userId == userBotAdminId;
    }

    private static void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {
        TdApi.AuthorizationState authorizationState = update.authorizationState;
        String state = "";
        if (authorizationState instanceof TdApi.AuthorizationStateReady) state = "Logged in";
        else if (authorizationState instanceof TdApi.AuthorizationStateClosing) state = "Closing...";
        else if (authorizationState instanceof TdApi.AuthorizationStateClosed) state = "Closed";
        else if (authorizationState instanceof TdApi.AuthorizationStateLoggingOut) state = "Logging out...";
        log.info(state);
    }

    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        TdApi.MessageSender messageSender = update.message.senderId;
        if(messageSender instanceof TdApi.MessageSenderUser) {
            TdApi.MessageSenderUser messageSenderUser = (TdApi.MessageSenderUser) update.message.senderId;
            if(messageSenderUser.userId != userBotUserId) {
                onUpdateNewMessage(update, messageSenderUser);
            }
        } else {
            log.info(update.toString());
        }

    }
    private void onUpdateNewMessage(TdApi.UpdateNewMessage update, TdApi.MessageSenderUser messageSenderUser) {
        if(update.message.content instanceof TdApi.MessageText) {
            TdApi.MessageText messageText = (TdApi.MessageText) update.message.content;
            if(messageText.text.text.trim().equals("chatid")) {
                TdApi.SendMessage textReq = new TdApi.SendMessage();
                textReq.chatId = update.message.chatId;
                TdApi.InputMessageText textContent = new TdApi.InputMessageText();
                textContent.text = new TdApi.FormattedText("当前对话ChatId是：" + String.valueOf(update.message.chatId) + "\n请使用此ChatId在系统中与您的账号绑定", null);
                textReq.inputMessageContent = textContent;
                client.send(textReq, result -> {
                    // Handle the response if needed
                });
                return;
            }
        }
        Long userId = messageSenderUser.userId;
        //判断userId在账号库里有没有记录，识别过就取账号库里的用户名
        BotAccount botAccount = botAccountService.selectByBotId(userId);
        if(botAccount == null) {
            client.send(new TdApi.GetUser(userId), result -> {
                log.info("GetUser={}, result={}", userId, result);
                TdApi.User user = result.get();
                //保存入账号库
                BotAccount account = botAccountService.save(userId, user.usernames.editableUsername, user.firstName + user.lastName);
                if(account != null) {
                    log.info("SaveUser={} success!", userId);
                    //验证消息来源，解析格式，转发
                    process(update, account);
                } else {
                    log.info("SaveUser={} failure!", userId);
                }
            });
        } else {
            //验证消息来源，解析格式，转发
            process(update, botAccount);
        }
    }

    /**
     * 验证消息来源，解析格式，转发
     * @param update
     * @param botAccount
     */
    private void process(TdApi.UpdateNewMessage update, BotAccount botAccount) {
        Long userId = ((TdApi.MessageSenderUser) update.message.senderId).userId;
        long chatId = update.message.chatId;
        //1.验证消息来源，查看是否来自指定的机器人消息
        HubGroupBot hubGroupBot = hubGroupBotService.selectByChatIdAndBotId(chatId, userId);
        if(hubGroupBot == null) {
            //非设置要采集消息的对象时，不做任何消息处理。
            log.info("Message from unknown user id={} and username={}, don't process it.", userId, botAccount.getUsername());
            return;
        }
        //处理消息
        log.info("recv msg from user id={} username={}, content={}", userId, botAccount.getUsername(), update.toString());
        TdApi.MessageContent messageContent = update.message.content;
        // 分析消息内容，转发至通知机器人，并回复原消息
        if (messageContent instanceof TdApi.MessageText) {
            TdApi.MessageText messageText = (TdApi.MessageText) update.message.content;
            String text = messageText.text.text;
            if(text.contains("chatid")) {
                return;
            }
            TdApi.Message message = update.message;
            long msgId = update.message.id;
            long replyToMessageId = update.message.replyToMessageId;
            //分析消息内容
            log.info("get MessageText content1: " + text);
            //分析消息内容
            //第一种：直接是订单号
            //第二种：订单号+空格+内容
            text = text.replaceAll("订单反馈", "")
                    .replaceAll("订单ID:", "")
                    .replaceAll("\n", "");
            log.info("get MessageText content2: " + text);
            //进行查单动作
            confirmOrder(update, text, botAccount);
        } else if(messageContent instanceof TdApi.MessagePhoto) {
            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) update.message.content;
            TdApi.FormattedText caption = messagePhoto.caption;
            String captionText = caption.text;
            log.info("get MessagePhoto content3: " + captionText);
            //分析消息内容
            //第一种：直接是订单号
            //第二种：订单号+空格+内容
            //第三种：订单反馈\n订单ID:\n202312140332429757485153
            captionText = captionText.replaceAll("订单反馈", "")
                    .replaceAll("订单ID:", "")
                    .replaceAll("\n", "");
            log.info("get MessagePhoto content4: " + captionText);
            //进行查单动作
            confirmOrder(update, captionText, botAccount);
        }
    }

    /**
     * 查单
     *
     * @param update
     * @param captionText
     * @param botAccount
     */
    private void confirmOrder(TdApi.UpdateNewMessage update, String captionText, BotAccount botAccount) {
        String[] captionTexts = captionText.split(" ");
        String orderNo = captionTexts.length > 0 ? captionTexts[0] : null;
        String memo = captionTexts.length > 1 ? captionTexts[1] : null;
        if(botAccount == null || ObjectUtils.isEmpty(orderNo)) return;
        //TODO 查单
        //根据消息来源方，查询对应的群组和三方信息
        List<HubGroupBot> hubGroupBotList = hubGroupBotService.selectByBotId(botAccount.getId());
        if(ObjectUtils.isEmpty(hubGroupBotList)) return;
        for(HubGroupBot hubGroupBot : hubGroupBotList) {
            HubGroup hubGroup = (HubGroup) hubGroupService.getMapper().selectByPrimaryKey(hubGroupBot.getGroupId());
            if(hubGroup != null) {
                HubInfo hubInfo = hubInfoService.selectByPrimaryKey(hubGroup.getHubId());
                if(hubInfo != null) {
                    //2.向三方接口发送请求，查询该订单绑定的码商信息
                    JSONObject jsonObject = zyAdapter.getBindChatId(hubInfo, orderNo);
                    int code = jsonObject.getIntValue("code");
                    String message = jsonObject.getString("message");
                    if(code == 200) {
                        //3.查单通知进行转发
                        String data = jsonObject.getString("data");
                        Long notifyChatId = Long.parseLong(data);
                        if(botStater != null && botStater.getNotifyBot() != null) {
                            log.info("confirmOrder: {} 转发查单消息 to {}", orderNo, notifyChatId);
                            //直接用userbot转发查单消息
                            sendConfirmOrderMsg(update, notifyChatId, orderNo);
                            message = orderNo + " 查单消息已转发";
                        }
                    } else {
                        message = orderNo + " " + message;
                    }
                    //4.回复四方机器人消息
                    log.info("confirmOrder: {} 回复四方机器人消息 to {}", orderNo, update.message.chatId);
                    replyGroupMessage(update, message);
                }
            }
        }

    }

    /**
     * 发送查单消息
     * @param update
     * @param notifyChatId
     * @param orderNo
     */
    private void sendConfirmOrderMsg(TdApi.UpdateNewMessage update, Long notifyChatId, String orderNo) {
        //TODO 下载消息内容，通过bot进行点对点通知查单
        if(update.message.content instanceof TdApi.MessagePhoto) {
            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) update.message.content;
            TdApi.DownloadFile downloadFile = new TdApi.DownloadFile();
            downloadFile.fileId = messagePhoto.photo.sizes[0].photo.id;
            downloadFile.priority = 1;
            downloadFile.synchronous = true;
            client.send(downloadFile, file -> {
                log.info("downloadFile: {}", file.get().local.path);
                if(!ObjectUtils.isEmpty(file.get().local.path)) {
                    //TODO 機器人發送消息
                    botStater.getNotifyBot().sendImageText(file.get().local.path, messagePhoto.caption.text, notifyChatId);
                }
            });
        }

        //user bot转发查单消息
//        client.send(new TdApi.ForwardMessages(notifyChatId,
//                update.message.messageThreadId, update.message.chatId,
//                new long[]{update.message.id}, null, false, false, false), result -> {
//        });
        //user bot发送查单文本消息
        TdApi.SendMessage textReq = new TdApi.SendMessage();
//            textReq.replyToMessageId = update.message.replyToMessageId;
        textReq.chatId = notifyChatId;
        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
        textContent.text = new TdApi.FormattedText("查单 " + orderNo, null);
        textReq.inputMessageContent = textContent;
        client.send(textReq, result -> {
            // Handle the response if needed
        });
    }

    private void replyGroupMessage(TdApi.UpdateNewMessage update, String message) {
        TdApi.SendMessage textReq = new TdApi.SendMessage();
        textReq.replyToMessageId = update.message.id;
        textReq.chatId = update.message.chatId;
        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
        textContent.text = new TdApi.FormattedText(message, null);
        textReq.inputMessageContent = textContent;
        client.send(textReq, result -> {
            // Handle the response if needed
        });
    }
}
