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
import org.apache.ydata.service.RedisUtils;
import org.apache.ydata.service.hub.BotAccountService;
import org.apache.ydata.service.hub.HubGroupBotService;
import org.apache.ydata.service.hub.HubGroupService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.vo.SystemSettingsKeys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Resource
    RedisUtils redisUtils;
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    private IdUtil idUtil;

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
            if(messageText.text.text.trim().contains("chatid")) {
                TdApi.SendMessage textReq = new TdApi.SendMessage();
                textReq.chatId = update.message.chatId;
                TdApi.InputMessageText textContent = new TdApi.InputMessageText();
                textContent.text = new TdApi.FormattedText("当前对话ChatId：" + String.valueOf(update.message.chatId) , null);
                textReq.inputMessageContent = textContent;
                client.send(textReq, result -> {
                    // Handle the response if needed
                });
                return;
            }
            //纯文本内容不做转发，仅针对指定的消息内容解析
            //@yh_assistant_bot help
            //@yh_assistant_bot bind [ydata后台的三方系统ID编号]
            //@yh_assistant_bot listener [四方机器人username]
            String text = messageText.text.text.trim();
            if(text.contains("@" + userBotUsername)) {
                String cmd = text.replace("@" + userBotUsername, "").trim();
                log.info("process cmd : {}", cmd);
                if("help".equals(cmd)) {
                    //返回帮助信息
                    sendUserBotHelpInfo(update);
                } else if(cmd.startsWith("bind")) {
                    //拿要绑定的ID，进行绑定
                    bindChatId(cmd.replace("bind", "").trim(), update);
                } else if(cmd.startsWith("listener") || cmd.startsWith("listen")) {
                    //拿要监听的机器人账号，进行绑定
                    listenerBot(cmd.replace("listener", "").replace("listen", "").trim(), update);
                } else if(cmd.startsWith("unlistener") || cmd.startsWith("unlisten")) {
                    //拿要监听的机器人账号，进行绑定
                    unlistenerBot(cmd.replace("unlistener", "").replace("unlisten", "").trim(), update);
                }
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
                BotAccount account = botAccountService.save(userId,
                        user.usernames != null ? user.usernames.editableUsername : (user.firstName + user.lastName), user.firstName + user.lastName);
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
        log.info("recv msg from user id={} username={}", userId, botAccount.getUsername());
        TdApi.MessageContent messageContent = update.message.content;
        // 分析消息内容，转发至通知机器人，并回复原消息
        if (messageContent instanceof TdApi.MessageText) {
            //纯文本内容不做转发
        } else if(messageContent instanceof TdApi.MessagePhoto) {
            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) update.message.content;
            TdApi.FormattedText caption = messagePhoto.caption;
            String captionText = caption.text;
            log.info("get MessagePhoto content3: " + captionText);
            //分析消息内容
            //第一种：直接是订单号
            //第二种：订单号+空格+内容
            //第三种：订单反馈\n订单ID:\n202312140332429757485153
            String filterWords = redisUtils.getSysConfig(SystemSettingsKeys.BOT_ANALYSIS_FILTER_WORDS);
            if(!ObjectUtils.isEmpty(filterWords)) {
                List<String> filterWordList = Arrays.asList(filterWords.split("[|]"));
                if(!ObjectUtils.isEmpty(filterWordList)) {
                    for (String word : filterWordList) {
                        captionText = captionText.replaceAll(word, "");
                    }
                }
                captionText = captionText.replaceAll("\n", "");
            }
            log.info("get MessagePhoto content4: " + captionText);
            if(captionText.contains("系统订单号") && captionText.contains("通道订单号")) {
                captionText = captionText.substring(captionText.indexOf("系统订单号") + "系统订单号".length(),
                        captionText.indexOf("通道订单号")).replace(":", "").replace("：", "").trim();
                log.info("get MessagePhoto content5: " + captionText);
            }
            if(captionText.contains("订单创建时间")) {
                captionText = captionText.substring(0, captionText.indexOf("订单创建时间"));
                log.info("get MessagePhoto content6: " + captionText);
            }
            if(captionText.contains("商户订单号") && captionText.contains("系统订单号")) {
                captionText = captionText.substring(captionText.indexOf("系统订单号") + "系统订单号".length(),
                        captionText.indexOf("订 单  金 额")).replace(":", "").replace("：", "").trim();
                log.info("get MessagePhoto content7: " + captionText);
            }
            if(captionText.contains("订单编号") && captionText.contains("订单金额")) {
                captionText = captionText.substring(captionText.indexOf("订单编号") + "订单编号".length(),
                        captionText.indexOf("订单金额")).replace(":", "").replace("：", "").trim();
                log.info("get MessagePhoto content8: " + captionText);
            }
            if(captionText.contains("系统") && captionText.contains("订单金额")) {
                captionText = captionText.substring(captionText.indexOf("系统") + "系统".length(),
                        captionText.indexOf("订单金额")).replace(":", "").replace("：", "").trim();
                log.info("get MessagePhoto content9: " + captionText);
            }
            //进行查单动作
            confirmOrder(update, captionText, botAccount);
        }
    }

    /**
     * 发送帮助信息，指导如何进行系统绑定
     * @param update
     */
    private void sendUserBotHelpInfo(TdApi.UpdateNewMessage update) {
        TdApi.SendMessage textReq = new TdApi.SendMessage();
        textReq.chatId = update.message.chatId;
        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
        textContent.text = new TdApi.FormattedText("绑定步骤：\n1.使用 \"@" + userBotUsername
                + "  bind  [systemId]\" 命令，将群组绑定到系统；\n" +
                "2.使用 \"@" + userBotUsername + "  listener  [@目标机器人]\" 命令，监听指定机器人的查单消息。",
                null);
        textReq.inputMessageContent = textContent;
        client.send(textReq, result -> {
            // Handle the response if needed
        });
    }

    /**
     * 绑定三方系统的chatid
     * @param systemId 要绑定的id
     * @param update
     */
    private void bindChatId(String systemId, TdApi.UpdateNewMessage update) {
        //查询有没有这个systemId
        HubInfo hubInfo = hubInfoService.selectByPrimaryKey(Long.parseLong(systemId));
        if(hubInfo == null) {
            //回复：要绑定的systemId错误，请联系技术人员索要！
            TdApi.SendMessage textReq = new TdApi.SendMessage();
            textReq.chatId = update.message.chatId;
            textReq.replyToMessageId = update.message.id;
            TdApi.InputMessageText textContent = new TdApi.InputMessageText();
            textContent.text = new TdApi.FormattedText("要绑定的systemId错误，请联系技术人员索要！",
                    null);
            textReq.inputMessageContent = textContent;
            client.send(textReq, result -> {
                // Handle the response if needed
            });
            return;
        }
        //创建群组信息
        HubGroup hubGroup = hubGroupService.selectByHubIdAndChatId(Long.parseLong(systemId), update.message.chatId);
        if(hubGroup == null) {
            HubGroup group = new HubGroup();
            group.setId(idUtil.nextId());
            group.setHubId(Long.parseLong(systemId));
            group.setHubName(systemId);
            group.setTelChatId(update.message.chatId);
            group.setTelGroupName(String.valueOf(update.message.chatId));
            group.setCreated(System.currentTimeMillis());
            group.setState(1);
            hubGroupService.getMapper().insert(group);
        }
        //回复绑定成功
        TdApi.SendMessage textReq = new TdApi.SendMessage();
        textReq.chatId = update.message.chatId;
        textReq.replyToMessageId = update.message.id;
        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
        textContent.text = new TdApi.FormattedText("绑定完成！下一步设置要监听的对象",
                null);
        textReq.inputMessageContent = textContent;
        client.send(textReq, result -> {
            // Handle the response if needed
        });
    }

    /**
     * 设置要监听的机器人对象
     * @param listenerBotUserName
     * @param update
     */
    private void listenerBot(String listenerBotUserName, TdApi.UpdateNewMessage update) {
        //查询机器人信息
        BotAccount botAccount = botAccountService.selectByBotUsername(listenerBotUserName.replace("@", "").trim());
        if(botAccount == null) {
            //回复：未查询到要绑定的用户信息，请稍后重试
            TdApi.SendMessage textReq = new TdApi.SendMessage();
            textReq.chatId = update.message.chatId;
            textReq.replyToMessageId = update.message.id;
            TdApi.InputMessageText textContent = new TdApi.InputMessageText();
            textContent.text = new TdApi.FormattedText("未查询到要绑定的用户信息，请稍后重试！",
                    null);
            textReq.inputMessageContent = textContent;
            client.send(textReq, result -> {
                // Handle the response if needed
            });
            return;
        }
        //绑定机器人
        HubGroup hubGroup = hubGroupService.selectByChatId(update.message.chatId);
        if(hubGroup == null) {
            TdApi.SendMessage textReq = new TdApi.SendMessage();
            textReq.chatId = update.message.chatId;
            textReq.replyToMessageId = update.message.id;
            TdApi.InputMessageText textContent = new TdApi.InputMessageText();
            textContent.text = new TdApi.FormattedText("请先绑定系统群组，再设置要监听的机器人！",
                    null);
            textReq.inputMessageContent = textContent;
            client.send(textReq, result -> {
                // Handle the response if needed
            });
            return;
        }
        //查询是否已设置过机器人
        HubGroupBot hubGroupBot = hubGroupBotService.selectByChatIdAndBotId(update.message.chatId, botAccount.getId());
        if(hubGroupBot == null) {
            hubGroupBot = new HubGroupBot();
            hubGroupBot.setGroupId(hubGroup.getId());
            hubGroupBot.setTelChatId(update.message.chatId);
            hubGroupBot.setTelBotId(botAccount.getId());
            hubGroupBot.setTelBotUsername(botAccount.getUsername());
            hubGroupBot.setCreated(System.currentTimeMillis());
            hubGroupBot.setState(1);
            hubGroupBotService.getMapper().insert(hubGroupBot);
        }
        //回复：设置监听成功
        TdApi.SendMessage textReq = new TdApi.SendMessage();
        textReq.chatId = update.message.chatId;
        textReq.replyToMessageId = update.message.id;
        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
        textContent.text = new TdApi.FormattedText("设置监听成功！",
                null);
        textReq.inputMessageContent = textContent;
        client.send(textReq, result -> {
            // Handle the response if needed
        });
    }

    /**
     * 设置要取消监听的机器人对象
     * @param listenerBotUserName
     * @param update
     */
    private void unlistenerBot(String listenerBotUserName, TdApi.UpdateNewMessage update) {
        //查询机器人信息
        BotAccount botAccount = botAccountService.selectByBotUsername(listenerBotUserName.replace("@", "").trim());
        if(botAccount == null) {
            //回复：未查询到要绑定的用户信息，请稍后重试
            TdApi.SendMessage textReq = new TdApi.SendMessage();
            textReq.chatId = update.message.chatId;
            textReq.replyToMessageId = update.message.id;
            TdApi.InputMessageText textContent = new TdApi.InputMessageText();
            textContent.text = new TdApi.FormattedText("未查询到要解除监听的用户信息，请稍后重试！",
                    null);
            textReq.inputMessageContent = textContent;
            client.send(textReq, result -> {
                // Handle the response if needed
            });
            return;
        }
        //查询是否已设置过机器人
        HubGroupBot hubGroupBot = hubGroupBotService.selectByChatIdAndBotId(update.message.chatId, botAccount.getId());
        if(hubGroupBot != null) {
            hubGroupBotService.getMapper().delete(hubGroupBot);
        }
        //回复：设置监听成功
        TdApi.SendMessage textReq = new TdApi.SendMessage();
        textReq.chatId = update.message.chatId;
        textReq.replyToMessageId = update.message.id;
        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
        textContent.text = new TdApi.FormattedText("解除监听成功！",
                null);
        textReq.inputMessageContent = textContent;
        client.send(textReq, result -> {
            // Handle the response if needed
        });
    }

    /**
     * 查单
     *
     * @param update
     * @param captionText
     * @param botAccount
     */
    private void confirmOrder(TdApi.UpdateNewMessage update, String captionText, BotAccount botAccount) {
//        log.info("查单消息01：{}", update.toString());
        String[] captionTexts = captionText.trim().split(" ");
        String orderNo = captionTexts.length > 0 ? captionTexts[0] : null;
        log.info("查单消息02，订单号：{}", orderNo);
        String memo = captionTexts.length > 1 ? captionTexts[1] : null;
        if(botAccount == null || ObjectUtils.isEmpty(orderNo)) return;
        //TODO 查单
        //根据消息来源方，查询对应的群组和三方信息
        HubGroupBot hubGroupBot = hubGroupBotService.selectByChatIdAndBotId(update.message.chatId, botAccount.getId());
        if(hubGroupBot == null) return;
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
                    JSONObject data = null;
                    if(jsonObject.get("data") instanceof String) {
                        data = new JSONObject();
                        data.put("targetChatId", jsonObject.getString("data"));
                    } else if(jsonObject.get("data") instanceof JSONObject) {
                        data = jsonObject.getJSONObject("data");
                        //判断data是否有orderState字段，有则判断状态，已回调则不转发
                        if(data.containsKey("orderState") && data.getIntValue("orderState") == 1) {
                            //订单已成功
                            message = orderNo + " 已补单";
                            replyGroupMessage(update, message);
                            return;
                        }
                    }
                    if(botStater != null && botStater.getNotifyBot() != null) {
                        log.info("confirmOrder: {} 转发查单消息 to {}", orderNo, data);
                        //直接用userbot转发查单消息
                        sendConfirmOrderMsg(update, data, orderNo);
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

    /**
     * 发送查单消息
     * @param update
     * @param data 从主服务获取的订单绑定信息
     * @param orderNo
     */
    private void sendConfirmOrderMsg(TdApi.UpdateNewMessage update, JSONObject data, String orderNo) {
        //TODO 下载消息内容，通过bot进行点对点通知查单
        if(update.message.content instanceof TdApi.MessagePhoto) {
            TdApi.MessagePhoto messagePhoto = (TdApi.MessagePhoto) update.message.content;
            TdApi.DownloadFile downloadFile = new TdApi.DownloadFile();
            downloadFile.fileId = messagePhoto.photo.sizes[messagePhoto.photo.sizes.length-1].photo.id;
            downloadFile.priority = 1;
            downloadFile.synchronous = true;
            client.send(downloadFile, file -> {
                log.info("downloadFile: {}", file.get().local.path);
                if(!ObjectUtils.isEmpty(file.get().local.path)) {
                    //TODO 機器人發送消息
                    String caption = messagePhoto.caption.text;
                    if(data.containsKey("channelCodeName")) {
                        caption += "\n订单通道：" + data.getString("channelCodeName");
                    }
                    if(data.containsKey("orderNo")) {
                        caption += "\n订单号：" + data.getString("orderNo");
                    }
                    if(data.containsKey("username")) {
                        caption += "\n码商：" + data.getString("username");
                    }
                    if(data.containsKey("telAccount") && !ObjectUtils.isEmpty(data.getString("telAccount"))) {
                        caption += " @" + data.getString("telAccount").replace("@","");
                    }
                    botStater.getNotifyBot().sendImageText(file.get().local.path, caption,
                            data.containsKey("targetChatId") ? Long.parseLong(data.getString("targetChatId").trim()) : null,
                            data.containsKey("orderNo") ? data.getString("orderNo") : "");
                }
            });
            //TODO 将orderNo 对应的 msgid 和 chatid 放入redis中绑定[有效期72小时]，用于回调响应
            if(data.containsKey("orderNo")) {
                redisTemplate.opsForValue().set(data.getString("orderNo"), update.message.chatId + ":" + update.message.id,
                        72, TimeUnit.HOURS);
            }
        }

        //user bot转发查单消息
//        client.send(new TdApi.ForwardMessages(notifyChatId,
//                update.message.messageThreadId, update.message.chatId,
//                new long[]{update.message.id}, null, false, false, false), result -> {
//        });
        //user bot发送查单文本消息
//        TdApi.SendMessage textReq = new TdApi.SendMessage();
////            textReq.replyToMessageId = update.message.replyToMessageId;
//        textReq.chatId = notifyChatId;
//        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
//        textContent.text = new TdApi.FormattedText("查单 " + orderNo, null);
//        textReq.inputMessageContent = textContent;
//        client.send(textReq, result -> {
//            // Handle the response if needed
//        });
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

    /**
     * 处理查单内联菜单点击结果
     * @param chatId
     * @param msgId
     * @param msg
     */
    public void replayCallbackQuery(String chatId, String msgId, String msg) {
        TdApi.SendMessage textReq = new TdApi.SendMessage();
        textReq.replyToMessageId = Long.parseLong(msgId);
        textReq.chatId = Long.parseLong(chatId);
        TdApi.InputMessageText textContent = new TdApi.InputMessageText();
        textContent.text = new TdApi.FormattedText(msg, null);
        textReq.inputMessageContent = textContent;
        client.send(textReq, result -> {
            // Handle the response if needed
        });
    }
}
