package org.apache.ydata.service.hub;

import org.apache.ydata.model.hub.HubGroupBot;
import org.apache.ydata.service.BaseService;

import java.util.List;

public interface HubGroupBotService extends BaseService {

    HubGroupBot selectByChatIdAndBotId(long chatId, Long botId);

    void deleteAllByGroupId(Long groupId);

    List<HubGroupBot> selectByGroupId(Long groupId);

    List<HubGroupBot> selectByBotId(Long botId);
}
