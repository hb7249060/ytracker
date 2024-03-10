package org.apache.ydata.model.hub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "hub_recharge_record")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HubRechargeRecord {

    @Transient
    String hubName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "BIGINT COMMENT '三方ID'")
    Long hubId;

    @Column(columnDefinition = "DECIMAL(10,2) COMMENT '充值金额'")
    Double amount;

    @Column(columnDefinition = "TEXT COMMENT '备注'")
    String memo;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

}
