package org.apache.ydata.vo;

/**
 * 系统设置项
 */
public interface SystemSettingsKeys {

    /** 报警机器人转发目标群聊 **/
    String ALARM_BOT_TARGET_CHAT_IDS = "ALARM_BOT_TARGET_CHAT_IDS";

    //查单转发内容过滤词，分析订单用
    String BOT_ANALYSIS_FILTER_WORDS = "BOT_ANALYSIS_FILTER_WORDS";

    //查单转发内容替换词
    String BOT_REPLACE_FILTER_WORDS = "BOT_REPLACE_FILTER_WORDS";
}
