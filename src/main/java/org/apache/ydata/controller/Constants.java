package org.apache.ydata.controller;

public interface Constants {

    String ERROR = "/common/errmsg.html";

    String ADMIN_LOGIN = "/admin/login.html";
    String ADMIN_INDEX = "/admin/index.html";
    String ADMIN_DASHBOARD = "/admin/dashboard.html";

    //业务相关
    //三方配置
    String CONFIG_HUBINFO_LIST = "/admin/business/hubinfo/list.html";
    String CONFIG_HUBINFO_ADD_OR_UPDATE = "/admin/business/hubinfo/addOrUpdate.html";
    String CONFIG_HUBINFO_VIEW_STAT = "/admin/business/hubinfo/viewStat.html";
    String CONFIG_HUBINFO_Add_FEE = "/admin/business/hubinfo/addFee.html";

    String BUSINESS_STAT_LIST = "/admin/business/stat/list.html";

    String BUSINESS_HUBGROUP_LIST = "/admin/business/hubgroup/list.html";
    String BUSINESS_HUBGROUP_ADD_OR_UPDATE = "/admin/business/hubgroup/addOrUpdate.html";

    String BUSINESS_BOT_ACCOUNT_LIST = "/admin/business/botaccount/list.html";

    //三方支付通道配置
    String CONFIG_HUB_PAY_CHANNEL_LIST = "/admin/config/hubpaychannel/list.html";
    String CONFIG_HUB_PAY_CHANNEL_ADD_OR_UPDATE = "/admin/config/hubpaychannel/addOrUpdate.html";

    //商户配置
    String CONFIG_MERCHANT_LIST = "/admin/config/mchinfo/list.html";
    String CONFIG_MERCHANT_ADD_OR_UPDATE = "/admin/config/mchinfo/addOrUpdate.html";

    //商户支付通道配置
    String CONFIG_MCH_PAY_CHANNEL_LIST = "/admin/config/mchpaychannel/list.html";
    String CONFIG_MCH_PAY_CHANNEL_ADD_OR_UPDATE = "/admin/config/mchpaychannel/addOrUpdate.html";

    String ORDERINFO_LIST = "/admin/business/orderinfo/list.html";
    String ORDERINFO_UPDATE = "/admin/business/orderinfo/update.html";

    //商户下单记录
    String MERCHANT_PO_RECORD_LIST = "/admin/business/merchant/po_record_list.html";

    //三方收益流水记录
    String HUB_PROFIT_RECORD_LIST = "/admin/business/hubprofitrecord/list.html";

    //四方收益流水记录
    String STAT_STAT = "/admin/systemstat/stat.html";
    String PAYORDER_STAT = "/admin/systemstat/payorder/stat.html";

    String HUBINFO_STAT = "/admin/systemstat/hubinfo/stat.html";
    String MERCHANT_STAT = "/admin/systemstat/merchant/stat.html";

    //系统相关
    String SYS_ADMIN_LIST = "/admin/system/admin/list.html";
    String SYS_ADMIN__ADD_OR_UPDATE = "/admin/system/admin/addOrUpdate.html";
    String SYS_LOGS = "/admin/system/logs.html";
    String SYS_SETTINGS = "/admin/system/settings.html";
    String SYS_PAY_CHANNEL_LIST = "/admin/system/paychannel/list.html";
    String SYS_PAY_CHANNEL_ADD_OR_UPDATE = "/admin/system/paychannel/addOrUpdate.html";

    //系统费充值
    String SYS_RECHARGE_RECORD_LIST = "/admin/system/rechargerecord/list.html";
    String SYS_RECHARGE_RECORD_ADD_OR_UPDATE = "/admin/system/rechargerecord/addOrUpdate.html";

    //商户积分充值
    String MCH_POINTS_LIST = "/admin/business/merchantpoints/list.html";
    String MCH_POINTS_ADD_OR_VIEW = "/admin/business/merchantpoints/addOrView.html";


    //下单测试
    String INDEX_PAY = "/index-pay.html";
}
