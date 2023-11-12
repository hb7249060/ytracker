package org.apache.ydata.vo.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class HubInfoVo implements Serializable {

    String googleCode;   //google code

    Long id;
    String name;
    Double rate;
    String apiUrl;
    String memo;
    Integer state;

}
