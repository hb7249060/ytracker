package org.apache.ydata.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MchPayChannelVo implements Serializable {

    String googleCode;   //google code

    Long id;

    String mchName;

    String sysChannelName;
    Double rate;

    Integer usable;

    String memo;

}
