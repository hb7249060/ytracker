package org.apache.ydata.model.hub;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 三方系统 数据
 */
@Data
@Entity
@Table(name = "hub_data")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HubData implements Serializable {

    @Transient
    String hubName;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "BIGINT COMMENT '三方ID'")
    Long hubId;

    @Column(columnDefinition = "VARCHAR(64) COMMENT '统计日期yyyyMMdd'")
    String statDate;

    @Column(columnDefinition = "BIGINT COMMENT '订单总数'")
    Long orderTotalCount;

    @Column(columnDefinition = "BIGINT COMMENT '订单成功总数'")
    Long orderTotalPayCount;

    @Column(columnDefinition = "DECIMAL(10,2) COMMENT '订单总金额'")
    Double orderTotalFee;

    @Column(columnDefinition = "DECIMAL(10,2) COMMENT '订单已支付金额'")
    Double orderPayFee;

    @Column(columnDefinition = "DECIMAL(10,2) COMMENT '成功率'")
    Double orderSuccessRate;

    @Column(columnDefinition = "DECIMAL(10,2) COMMENT '收益'")
    Double benfit;

    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long created;

    @Column(columnDefinition = "BIGINT COMMENT '更新时间'")
    Long updated;


}
