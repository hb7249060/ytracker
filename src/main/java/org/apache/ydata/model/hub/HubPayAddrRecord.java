package org.apache.ydata.model.hub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 三方系统 收银台地址变更记录
 */
@Data
@Entity
@Table(name = "hub_pay_addr_record")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HubPayAddrRecord implements Serializable {

    @Transient
    String targetHubIds;

    @Transient
    String hubName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "BIGINT COMMENT '三方ID'")
    Long hubId;

    @Column(columnDefinition = "TEXT COMMENT '支付服务地址'")
    String payAddr;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

    @Column(columnDefinition = "BIGINT COMMENT '更新时间'")
    Long updated;


}
