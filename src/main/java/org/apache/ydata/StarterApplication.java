package org.apache.ydata;

import org.apache.ydata.bot.MyBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class StarterApplication {

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot());
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
        SpringApplication.run(StarterApplication.class, args);
    }

}
