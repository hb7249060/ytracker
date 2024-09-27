package org.apache.ydata.bot.mmutils;

public interface MMMsgType {

    String help = "helpinfo";

    //根据ID绑定三方
    String bind = "bind";

    //绑定ID解绑三方
    String unbind = "unbind";

    //获取充值地址
    String walletAddress = "地址";

    //充值加款
    String addFee = "加款";

    //预借加款
    String jieFee = "预借";

    //系统费余额
    String balanceFee = "余额";

    //跑量
    String paoliang = "跑量";
    //明细
    String detail = "明细";

    //ID - 查询三方ID
    String id = "id";
}
