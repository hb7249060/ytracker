package org.apache.ydata.service.hub;

import org.apache.ydata.model.hub.BotAccount;
import org.apache.ydata.service.BaseService;

public interface BotAccountService extends BaseService {

    BotAccount selectByBotId(Long userId);

    BotAccount save(Long userId, String username, String nickname);
}
