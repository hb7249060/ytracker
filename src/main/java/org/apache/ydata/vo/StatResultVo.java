package org.apache.ydata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatResultVo implements Serializable {
    String statCondition;
    double rechargePoints;
    int orderTotalCount;
    int orderTotalPayCount;
    double orderTotalFee;
    double orderPayFee;
    double orderSuccessRate;
    String profit;
    String userProfit;
    String orderProfit;
}