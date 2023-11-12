package org.apache.ydata.vo.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayChannelVo implements Serializable {

    String googleCode;

    Long id;
    String code;
    String name;
    String[] state;
    String memo;

}
