package org.apache.ydata.vo.admin;

import lombok.Data;

@Data
public class AdminLoginVo {
    String username;
    String password;
    String googleCode;   //google code
}
