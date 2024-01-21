package org.apache.ydata.vo.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class HubGroupVo implements Serializable {

    Long id;

    Long hubId;
    String hubName;

    Long telChatId;

    String telGroupName;

    String memo;

    Integer state;

    String groupBotInfo;//组机器人信息
}
