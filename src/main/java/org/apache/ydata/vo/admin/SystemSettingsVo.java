package org.apache.ydata.vo.admin;

import lombok.Data;
import org.apache.ydata.vo.SystemSettingsKeys;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class SystemSettingsVo implements Serializable {

    String ALARM_BOT_TARGET_CHAT_IDS;
    String BOT_ANALYSIS_FILTER_WORDS;
    String BOT_REPLACE_FILTER_WORDS;

    String RECHARGE_ADDR;   //充值地址
    String RECHARGE_ADDR_IMG_URL;   //充值地址图片
    String RECHARGE_BOT_OPER;   //充值机器人操作员

    public Map<String, String> toDataMap() {
        HashMap<String, String > dataMap = new HashMap<>();
        dataMap.put(SystemSettingsKeys.ALARM_BOT_TARGET_CHAT_IDS, ALARM_BOT_TARGET_CHAT_IDS);
        dataMap.put(SystemSettingsKeys.BOT_ANALYSIS_FILTER_WORDS, BOT_ANALYSIS_FILTER_WORDS);
        dataMap.put(SystemSettingsKeys.BOT_REPLACE_FILTER_WORDS, BOT_REPLACE_FILTER_WORDS);
        dataMap.put(SystemSettingsKeys.RECHARGE_ADDR, RECHARGE_ADDR);
        dataMap.put(SystemSettingsKeys.RECHARGE_ADDR_IMG_URL, RECHARGE_ADDR_IMG_URL);
        dataMap.put(SystemSettingsKeys.RECHARGE_BOT_OPER, RECHARGE_BOT_OPER);
        return dataMap;
    }
}
