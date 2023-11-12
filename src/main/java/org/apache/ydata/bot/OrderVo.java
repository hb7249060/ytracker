package org.apache.ydata.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo implements Serializable {

    String orderNo;
    String tradeNo;

//    @Column(columnDefinition = "VARCHAR(255) COMMENT '口令'")
    String checkCode;

//    @Column(columnDefinition = "DECIMAL(10,2) COMMENT '订单实付金额'")
    Double fee;

//    @Column(columnDefinition = "DECIMAL(10,2) COMMENT '订单金额'")
    Double payFee;

//    @Column(columnDefinition = "BIGINT COMMENT '付款码ID'")
    Long payCodeId;

//    @Column(columnDefinition = "VARCHAR(24) COMMENT '收款码别名快照'")
    String payCodeAlias;    //收款码别名快照

//    @Column(columnDefinition = "TEXT COMMENT '收款码值快照'")
    String payCodeValue;

//    @Column(columnDefinition = "TINYINT COMMENT '通知状态，0未通知，1已通知'")
    Integer notifyState;

//    @Column(columnDefinition = "BIGINT COMMENT '通知时间'")
    Long notifyTime;

//    @Column(columnDefinition = "BIGINT COMMENT '支付时间'")
    Long payTime;

//    @Column(columnDefinition = "INT COMMENT '订单状态，0支付中，1已完成，-1支付失败'")
    Integer orderState;

//    @Column(columnDefinition = "BIGINT COMMENT '创建时间'")
    Long createTime;

    String memo;
}
