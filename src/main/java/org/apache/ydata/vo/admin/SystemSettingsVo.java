package org.apache.ydata.vo.admin;

import lombok.Data;
import org.apache.ydata.vo.SystemSettingsKeys;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class SystemSettingsVo implements Serializable {

    String ALARM_BOT_TARGET_CHAT_IDS;

    public Map<String, String> toDataMap() {
        HashMap<String, String > dataMap = new HashMap<>();
        dataMap.put(SystemSettingsKeys.ALARM_BOT_TARGET_CHAT_IDS, ALARM_BOT_TARGET_CHAT_IDS);
        return dataMap;
    }
}
