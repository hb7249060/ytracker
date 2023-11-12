package org.apache.ydata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatResultVo2 implements Serializable {
    String statCondition;
    String rechargePoints;
    String orderTotalCount;
    String orderTotalPayCount;
    String orderTotalFee;
    String orderPayFee;
    String orderSuccessRate;
    String profit;
    String userProfit;
    String orderProfit;
}