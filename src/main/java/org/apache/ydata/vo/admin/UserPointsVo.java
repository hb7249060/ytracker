package org.apache.ydata.vo.admin;

import lombok.Data;

@Data
public class UserPointsVo {
    String secPwd;   //google code
    String googleCode;   //google code
    Long userId;
    String userName;

    String eventType;

    Double points;

    String memo;
}
