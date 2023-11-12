package org.apache.ydata.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantVo  implements Serializable {

    String googleCode;   //google code

    Long id;

    String name;

    String publicKey;

    //放一份到redis中，与公钥对应
    String privateKey;

    String securityIp;
    Integer state;

    String memo;

    Integer poDirectEnable; //是否启用定向下发
    String targetHubIds; //定向下发三方系统ID
}
