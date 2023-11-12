package org.apache.ydata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardVo {

    int userCount;
    int payCodeCount;
    int payCodeAvaliCount;

    //今日
    String orderTotalCount;
    String orderTotalPayCount;
    String orderTotalFee;
    String orderPayFee;
    String orderSuccessRate;
    String rechargePoints;

    //昨日
    String orderTotalCount2;
    String orderTotalPayCount2;
    String orderTotalFee2;
    String orderPayFee2;
    String orderSuccessRate2;
    String rechargePoints2;

}
