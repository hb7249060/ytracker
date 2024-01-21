package org.apache.ydata.bot;

import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.service.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Configuration
public class BotStater {

    @Value("${config.telegram.alarmBot.enable}")
    private boolean alarmBotEnable;
    @Value("${config.telegram.alarmBot.username}")
    private String alarmBotUsername;
    @Value("${config.telegram.alarmBot.token}")
    private String alarmBotToken;

    @Value("${config.telegram.notifyBot.enable}")
    private boolean notifyBotEnable;
    @Value("${config.telegram.notifyBot.username}")
    private String notifyBotUsername;
    @Value("${config.telegram.notifyBot.token}")
    private String notifyBotToken;

    @Value("${config.telegram.userBot.enable}")
    private boolean userBotEnable;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private BotMsgSender botMsgSender;

    @Resource
    private BotMsgReceiver botMsgReceiver;

    @Resource
    private MyUserBot myUserBot;

    private MyNotifyBot notifyBot;
    private MyAlarmBot alarmBot;

    @PostConstruct
    public void start() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            if(alarmBotEnable) {
                MyAlarmBot myAlarmBot = new MyAlarmBot(botMsgReceiver, botMsgSender, alarmBotUsername, alarmBotToken);
                telegramBotsApi.registerBot(myAlarmBot);
                alarmBot = myAlarmBot;
            }
            if(notifyBotEnable) {
                MyNotifyBot myNotifyBot = new MyNotifyBot(botMsgReceiver, botMsgSender, notifyBotUsername, notifyBotToken);
                telegramBotsApi.registerBot(myNotifyBot);
                notifyBot = myNotifyBot;
            }
            if(userBotEnable) {
                 new Thread() {
                     @Override
                     public void run() {
                         super.run();
                         try {
                             myUserBot.init(BotStater.this);
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 }.start();
            }
            /*  Webhook方式
			Webhook webhook = new DefaultWebhook();
			//Webhooks_目前支持的端口：443, 80, 88, 8443
			webhook.setInternalUrl("http://0.0.0.0:8443");
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, webhook);
			MyWebHookBot myWebHookBot = new MyWebHookBot();
			//需要一个可以https访问的域名并转发到上面端口
			SetWebhook setWebhook = SetWebhook.builder().url("https://joelwublog.com/").build();
			telegramBotsApi.registerBot(myWebHookBot, setWebhook);
			*/
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public MyNotifyBot getNotifyBot() {
        return notifyBot;
    }

    public MyAlarmBot getAlarmBot() {
        return alarmBot;
    }
}
