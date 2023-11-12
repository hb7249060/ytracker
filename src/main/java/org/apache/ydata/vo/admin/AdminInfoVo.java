package org.apache.ydata.vo.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminInfoVo implements Serializable {

    String googleCode;   //google code

    Long id;
    String username;
    String password;
    String secPwd;
    String memo;
    Integer state;

}
