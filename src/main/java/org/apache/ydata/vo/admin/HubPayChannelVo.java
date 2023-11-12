package org.apache.ydata.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HubPayChannelVo implements Serializable {

    String googleCode;   //google code

    Long id;

    String hubName;

    String mchName;

    String bindMchIds;    //绑定的商户id列表

    String sysChannelName;

    Double rate;

    Integer weight;

    //====三方通道配置信息 开始
//    @Column(columnDefinition = "VARCHAR(64) COMMENT '通道类型：代收or代付'")
    String type;

//    @Column(columnDefinition = "VARCHAR(64) COMMENT '加密类型：自营or三方'")
    String encType;

//    @Column(columnDefinition = "VARCHAR(64) COMMENT '三方通道名称'")
    String confName;

//    @Column(columnDefinition = "VARCHAR(64) COMMENT '三方通道编码'")
    String confPayCode;

//    @Column(columnDefinition = "VARCHAR(64) COMMENT '三方对接资料：商户ID'")
    String confMchId;

//    @Column(columnDefinition = "VARCHAR(64) COMMENT '三方对接资料：商户公钥'")
    String confMchPubKey;

//    @Column(columnDefinition = "VARCHAR(64) COMMENT '三方对接资料：商户私钥'")
    String confMchPriKey;

//    @Column(columnDefinition = "TEXT COMMENT '三方对接资料：商户下单地址'")
    String confMchPoUrl;

//    @Column(columnDefinition = "TEXT COMMENT '三方对接资料：商户查单地址'")
    String confMchQoUrl;

//    @Column(columnDefinition = "TEXT COMMENT '三方对接资料：商户余额查询地址，代付类型时需要'")
    String confMchBalanceUrl;

//    @Column(columnDefinition = "TEXT COMMENT '三方对接资料：回调IP'")
    String confMchCallbackIp;

    //====三方通道配置信息 结束

    Integer usable;

    String memo;

}
