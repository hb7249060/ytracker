package org.apache.ydata.service.hub;

import org.apache.ydata.model.hub.HubGroup;
import org.apache.ydata.service.BaseService;

public interface HubGroupService extends BaseService {

    HubGroup selectByHubIdAndChatId(Long hubId, Long chatId);

    HubGroup selectByChatId(Long chatId);
}
